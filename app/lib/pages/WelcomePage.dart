import 'dart:async';

import 'package:flutter/material.dart';
import 'HomePage.dart';

class WelcomePage extends StatefulWidget {
  WelcomePage({Key key}) : super(key: key);

  // This widget is the welcome page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  @override
  _WelcomePageState createState() => _WelcomePageState();
}

class _WelcomePageState extends State<WelcomePage> {

  Timer timer;

  @override
  void initState() {
    super.initState();
    timer = new Timer(const Duration(milliseconds: 1500), () {
     try {
       // TODO: check if mounted
        Navigator.of(context).pushAndRemoveUntil(new MaterialPageRoute(
          builder: (BuildContext context) => new HomePage()), (//跳转到主页
          Route route) => route == null);
      } catch (e) {}
    });
  }

  @override
  void dispose() {
    timer.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Container(
      color: Colors.red,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text(
                'C',
                style: TextStyle(
                  fontWeight: FontWeight.w900,
                  decoration: TextDecoration.none,
                  fontStyle: FontStyle.italic,
                  color: Colors.white)
              ),
              Text(
                'SDN',
                style: TextStyle(
                  fontWeight: FontWeight.w900,
                  decoration: TextDecoration.none,
                  fontStyle: FontStyle.italic,
                  color: Colors.black)
              )
            ],
          ),
          Text(
            '全球最大中文IT社区',
            textAlign: TextAlign.center,
            style: TextStyle(
              fontWeight: FontWeight.normal,
              fontSize: 15,
              decoration: TextDecoration.none,
              color: Colors.black)
          )
        ],
      )
    );
  }

}
