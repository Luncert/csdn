import 'package:flutter/material.dart';
import 'package:dio/dio.dart';
import 'dart:convert' show json, utf8;

class ArticleRef {
  String id; // article id
  String userName; // user id
  String title; // 标题
  String avatar; // 作者头像
  String nickname; // 作者昵称
  String createdAt; // 创建时间
  String summary; // 总结
  List<String> tag; // 标签
  int views; // 阅读量

  ArticleRef(var jsonData)
  {
    this.id = jsonData['id'];
    this.userName = jsonData['userName'];
    this.title = jsonData['title'];
    this.avatar = jsonData['avatar'];
    this.nickname = jsonData['nickname'];
    this.createdAt = jsonData['createdAt'];
    this.summary = jsonData['summary'];
    this.tag = jsonData['tag'];
    this.views = jsonData['views'];
  }
}

class HomePage extends StatefulWidget {
  HomePage({Key key}) : super(key: key);

  // This widget is the welcome page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {

  final List<ArticleRef> articleRefs = [];

  @override
  void initState() {
    super.initState();
    getMoreArticles(0);
  }

  void getMoreArticles(int shownOffset) async {
    try {
      final String url = 'http://127.0.0.1:5000/article';
      Response response;
      response = await Dio().get(url);
      return print(response);
      // if (rep.statusCode == 200) {
      //   String repBody = await rep.transform(utf8.decoder).join();
      //   var jsonData = json.decode(repBody);
      //   await for (var item in jsonData) {
      //     articleRefs.add(new ArticleRef(item));
      //   }
      // } else print("error");
    } catch (e) {
      return print(e);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: _buildList()
    );
  }

  Widget _buildList() {
  return ListView.builder(
      padding: const EdgeInsets.all(16.0),
      itemBuilder: (context, i) {
        if (i.isOdd) return Divider();

        final index = i ~/ 2;
        if (index >= articleRefs.length) {
          articleRefs.addAll(getMoreArticles(0));
        }
        return _buildRow(articleRefs[index]);
      }
    );
  }

  Widget _buildRow(ArticleRef articleRef) {
    return ListTile(
      title: Text(
        articleRef.title,
        style: TextStyle(fontSize: 18.0),
      ),
    );
  }

}