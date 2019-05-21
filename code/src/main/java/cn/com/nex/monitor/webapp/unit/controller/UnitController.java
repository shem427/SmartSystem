package cn.com.nex.monitor.webapp.unit.controller;

import cn.com.nex.monitor.webapp.common.MessageService;
import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import cn.com.nex.monitor.webapp.common.bean.StringValueBean;
import cn.com.nex.monitor.webapp.common.util.MonitorUtil;
import cn.com.nex.monitor.webapp.unit.bean.ImportUnitBean;
import cn.com.nex.monitor.webapp.unit.bean.UnitBean;
import cn.com.nex.monitor.webapp.unit.service.UnitService;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.apache.poi.util.IOUtils;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReadStatus;
import org.jxls.reader.XLSReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/unit")
public class UnitController {
    /** LOG */
    private static final Logger LOG = LoggerFactory.getLogger(UnitController.class);

    @Value("${cn.com.nex.monitor.top.unit}")
    private String topUnitId;

    @Autowired
    private UnitService unitService;

    @Autowired
    private MessageService messageService;

    @GetMapping(value = "/index")
    public String page() {
        return "unit/page";
    }

    @GetMapping(value = "/import")
    public String importPage() {
        return "unit/unitImportModal";
    }

    /**
     * 获取下位组织信息。
     * @param id ID
     * @return 下位组织信息
     */
    @GetMapping(value = "/subUnit")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public List<UnitBean> getSubUnit(String id) {
        return unitService.getSubUnit(id);
    }

    @GetMapping(value = "/subUnitByUser")
    @ResponseBody
    public List<UnitBean> getSubUnitByUser(String id) {
        UserBean self = MonitorUtil.getUserFromSecurity();
        if (id == null) {
            id = topUnitId;
        }
        return unitService.getSubUnitByUser(self.getUserId(), id);
    }

    @GetMapping(value = "/addPage")
    public ModelAndView getAddPage(String parentId, String parentName) {
        Map<String, Object> model = new HashMap<>();
        model.put("parentId", parentId);
        model.put("parentName", parentName);

        return new ModelAndView("unit/unitUpdateModal", model);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/editPage")
    public ModelAndView getEditPage(String parentId,
                                    String parentName,
                                    String unitId,
                                    String unitName,
                                    String unitRemark,
                                    boolean isParent) {
        Map<String, Object> model = new HashMap<>();
        List<UserBean> managers = unitService.getUnitManagers(unitId);

        model.put("parentId", parentId);
        model.put("parentName", parentName);
        model.put("unitId", unitId);
        model.put("unitName", unitName);
        model.put("unitRemark", unitRemark);
        model.put("managers", managers);
        model.put("isParent", isParent);

        return new ModelAndView("unit/unitUpdateModal", model);
    }

    @PostMapping(value = "/saveUnit")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public CommonBean saveUnit(UnitBean unit) {

        CommonBean bean = new CommonBean();
        try {
            String unitId = unit.getId();
            if (unitId != null) {
                // edit
                unitService.edit(unit);
            } else {
                // add
                unitService.add(unit);
            }
        } catch (Exception e) {
            MonitorUtil.handleException(e, bean, messageService);
        }

        return bean;
    }

    @PostMapping(value = "deleteUnit")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public CommonBean deleteUnit(String unitId) {
        CommonBean retBean = new CommonBean();
        try {
            boolean hasChildren = unitService.hasChildren(unitId);
            if (hasChildren) {
                // 有下位组织，不能删除
                String message = messageService.getMessage("mr.delete.unit.hasChildren");
                retBean.setStatus(CommonBean.Status.WARNING);
                retBean.setMessage(message);
            } else {
                unitService.delete(unitId);
            }
        } catch (Exception e) {
            MonitorUtil.handleException(e, retBean, messageService);
        }
        return retBean;
    }

    @GetMapping(value = "getManagers")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public List<UserBean> getUnitManagers(String unitId) {
        List<UserBean> managerList = unitService.getUnitManagers(unitId);
        return managerList;
    }

    @GetMapping(value = "getUnitFullPath")
    @ResponseBody
    public StringValueBean getUnitFullPath(String unitId) {
        StringValueBean retBean = new StringValueBean();
        try {
            String fullPath = unitService.getUnitFullPath(unitId);
            retBean.setValue(fullPath);
        } catch (Exception e) {
            MonitorUtil.handleException(e, retBean, messageService);
        }
        return retBean;
    }

    @GetMapping(value = "unitSelectModal")
    public String unitSelectModal() {
        return "unit/unitSelectModal";
    }

    @GetMapping(value = "/downloadTemplate")
    public void downloadUnitTemplate(HttpServletResponse response) throws IOException {
        InputStream template = UnitController.class.getResourceAsStream("/templet/unitsTemplate.xlsx");
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=unitsTemplate.xlsx");

            byte[] buffer = new byte[4096];
            int i = template.read(buffer);
            while (i != -1) {
                response.getOutputStream().write(buffer, 0, i);
                i = template.read(buffer);
            }
            response.flushBuffer();
        } finally {
            template.close();
        }
    }

    @PostMapping(value = "importUnit")
    @ResponseBody
    public CommonBean importUnit(@RequestParam("file") MultipartFile file) {
        List<ImportUnitBean> unitList = new ArrayList<>();
        Map<String, List<ImportUnitBean>> beans = new HashMap<>();
        beans.put("units", unitList);

        CommonBean bean = new CommonBean();
        InputStream xmlStream = new BufferedInputStream(UnitController.class
                .getResourceAsStream("/templet/units.xml"));
        InputStream xlsStream = null;
        XLSReader mainReader = null;
        try {
            xlsStream = file.getInputStream();
            mainReader = ReaderBuilder.buildFromXML(xmlStream);
            XLSReadStatus status = mainReader.read(xlsStream, beans);

            if (!status.isStatusOK()) {
                bean.setStatus(CommonBean.Status.ERROR);
                bean.setMessage("组织导入出错。请确认上传文件所使用的模板是否正确，内容是否正确。");
            } else {
                // TODO: import.
            }
        } catch (Exception e) {
            MonitorUtil.handleException(e, bean, messageService);
        } finally {
            IOUtils.closeQuietly(xlsStream);
            IOUtils.closeQuietly(xmlStream);
        }

        return bean;
    }
}
