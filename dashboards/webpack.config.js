'use strict';

const path = require('path');
const webpack = require('webpack');

module.exports ={
  target: 'web',
  entry: './src/App.ts',
  mode: 'development',
  output: {
    filename: 'bundle.js', //编译后的文件
    path: path.resolve(__dirname, 'dist')
  },
  module: {
    rules: [
      { test: /\.cson$/, loader: "cson-loader" },
      { test: /\.node$/, loader: "node-loader" },
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
      extensions: [ '.ts', '.js', '.scss' ]
  },
  plugins: [
    new webpack.WatchIgnorePlugin([
      /css\.d\.ts$/
    ]),
    new webpack.optimize.UglifyJsPlugin({
      compress: {warnings: false},
      output: {comments: false},
      sourceMap: true
    })
  ]
};