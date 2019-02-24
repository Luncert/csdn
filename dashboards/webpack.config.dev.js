'use strict';

const path = require('path');
const webpack = require('webpack');

module.exports ={
  target: 'electron-renderer',
  entry: './src/App.ts',
  mode: 'development',
  output: {
    filename: 'bundle.js', //编译后的文件
    path: path.resolve(__dirname, 'dist')
  },
  devServer: {
    contentBase: path.join(__dirname, "dist"), //编译好的文件放在这里
    compress: true,
    port: 9000 //本地开发服务器端口
  },
  module: {
    rules: [
      { test: /\.node$/, loader: "node-loader" },
      { test: /\.coffee$/, loader: "coffee-loader" },
      { test: /\.ts$/, loader: "ts-loader" },
      { test: /\.css$/,
        use: [
          'style-loader',
          {
            loader: 'css-loader',
            options: {
              modules: true,
              importLoaders: 1
            }
          }
        ]
      },
      { test: [/\.jpeg$/, /\.mp3$/],
        use: [
            {
                loader: 'url-loader',
                options: {
                    limit: '10240'
                }
            },
        ]
      }
    ]
  },
  resolve: {
      extensions: [ '.ts', '.js', '.css' ]
  }
};