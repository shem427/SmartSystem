const glob = require('glob');
const path = require('path');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');

const buildEntryConfig = () => {
    const rootPath = './src/main/resources/static';
    const entryMap = {};
    glob.sync(`${rootPath}/script-dev/*.js`).forEach(entry => {
        const name = entry.replace(`${rootPath}/script-dev`, '').replace('.js', '');
        entryMap[name] = entry;
    });
    return entryMap;
};

module.exports = {
    entry: buildEntryConfig(),
    output: {
        filename: '[name].js',
        path: path.resolve(__dirname, 'target/classes/static/script'),
    },
    plugins: [
        new UglifyJsPlugin()
    ]
};