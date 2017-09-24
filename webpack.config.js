var webpack = require('webpack');
var path = require('path');

module.exports = {
    context: path.join(__dirname, "src/main"),
    entry: "./javascripts/main.ts",
    output: {
        path: path.join(__dirname, 'src/main/resources/static/build'),
        publicPath: "/static/build/",
        filename: "bundle.js"
    },
    module: {
        loaders: [
            {
                test: /\.tsx?$/,
                loader: ["babel-loader?presets[]=env", "ts-loader"]
            },
            {
                test: /\.scss$/,
                loader: ["style-loader", "css-loader", "sass-loader"]
            }
        ]
    },
    devServer: {
            port: 9090,
            proxy: {
                '**': {
                    target: 'http://localhost:8080',
                    secure: false,
                    // <a href="https://github.com/nodejitsu/node-http-proxy">node-http-proxy</a> option - don't add /localhost:8080/ to proxied request paths
                    prependPath: false
                }
            },
            publicPath: 'http://localhost:9090/static/build/'
        },
    plugins: []
};