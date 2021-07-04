const path = require('path')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const { CleanWebpackPlugin } = require('clean-webpack-plugin')
const webpack = require('webpack')

const ASSET_PATH = '/'

module.exports = {
  entry: {
    main: './src/index.js'
  },
  output: {
    path: path.resolve(__dirname, '../../resources/static'),
    publicPath: ASSET_PATH
  },
  mode: 'development',
  devtool: 'source-map',
  resolve: {
    modules: [
      './node_modules',
      path.resolve('./src'),
      path.resolve('./')
    ],
    extensions: ['.wasm', '.mjs', '.js', '.json', '.jsx', '.tsx', '.ts']
  },
  module: {
    rules: [{
      test: /\.(js|ts)x?$/,
      exclude: /(node_modules|bower_components)/,
      use: [{
        loader: 'babel-loader',
        options: {
          presets: ['@babel/preset-env', '@babel/preset-react', '@babel/preset-typescript'],
          plugins: [
            '@babel/plugin-transform-runtime'
          ]
        }
      }]
    },
    {
      test: /\.(png|PNG|jpg|JPG|gif|GIF|svg|SVG|cur|ico)$/,
      use: [{
        loader: 'file-loader',
        options: {}
      }]
    },
    {
      test: /\.(woff|woff2|eot|ttf|otf|ico|md)(\?cl=0)?$/,
      loader: 'file-loader'
    },
    {
      test: /\.html$/,
      use: [{
        loader: 'tpl-loader'
      }]
    },

    {
      test: /\.xml$/,
      use: [{
        loader: 'raw-loader'
      }]
    },
    {
      test: /\.css$/i,
      use: ['style-loader', 'css-loader']
    }
    ]
  },
  plugins: [
    new webpack.IgnorePlugin(
      /^\.\/locale$/,
      /moment/,
      /\.eot$/,
      /\.ttf$/,
      /\.otf$/
    ), // Exclude moment locales in bundles (load explicitly in js file),
    new HtmlWebpackPlugin({
      filename: '../templates/index.html',
      title: 'Purchase Request'
    })
  ]
}
