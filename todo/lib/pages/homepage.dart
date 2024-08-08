import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter/rendering.dart';
import 'package:todo/pages/todo.dart';
import 'package:todo/listModel/toDo.dart';


class MyHomePage extends StatefulWidget
{
  MyHomePage({Key? key}) : super(key: key);

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final todoL=Todo.todoList();
  List<Todo> _foundTodo=[];
  final _todoController= TextEditingController();

  @override
  void initState()
  {
    _foundTodo=todoL;
    super.initState();
  }
  @override
  Widget build(BuildContext context)
  {
    return Scaffold(
      backgroundColor: Colors.purpleAccent,
      appBar: buildAppBar(),
        body: Stack(
          children: [
            Container(

              padding: const EdgeInsets.symmetric(horizontal: 20,vertical: 15),
              child: Column(
                children: [
                  searchBox(),
                  Expanded(
                    child: ListView(
                      padding: const EdgeInsets.only(bottom: 80),
                      children: [
                        Container(
                          margin: const EdgeInsets.only(
                              top: 50,
                              bottom: 20,
                          ),
                          child: const Text(
                            'List',
                            style: TextStyle(
                              fontSize: 30,
                              fontWeight: FontWeight.w500,
                            ),
                          ),
                        ),
                        for(Todo todoo in _foundTodo.reversed)
                          ToDoList(todo: todoo,
                          onToDoChange: _handleToChange,
                          onDelete: _deleteItem ,
                          ),
                      ],
                    ),
                  )
                ],
              ),
            ),
            Align(
              alignment: Alignment.bottomCenter,
              child: Row(children: [
                Expanded(
                    child: Container(
                      padding: EdgeInsets.symmetric(horizontal: 20,),
                      margin: const EdgeInsets.only(
                        bottom: 20,
                        right: 10,
                        left: 20,
                      ),
                      decoration:  BoxDecoration(
                        borderRadius: BorderRadius.circular(20),
                        color: Colors.white,

                      ),
                      child:  TextField(
                        controller: _todoController,
                        decoration: InputDecoration(
                          hintText: '  new activity',
                          border: InputBorder.none,

                        ),

                      ),
                    ),
                ),
                Container(
                  margin: const EdgeInsets.only(
                    bottom: 20,
                    right: 20,
                  ),
                  child: ElevatedButton(
                    child: const Text(
                      '+',
                      style: TextStyle(
                        fontSize: 40,
                      ),
                    ),
                    onPressed: () {
                      _addTodoItem(_todoController.text);
                    },
                    style: ElevatedButton.styleFrom(
                      //backgroundColor: Colors.blue,
                      //maximumSize: const Size(5,5),
                      elevation: 10,
                  ),
                ),
                ),
              ]),
            )
          ],
        ),

    );
  }

  void _handleToChange(Todo todo)
  {
    setState(() {
      todo.check=!todo.check;
    });
  }

  void _deleteItem(String id)
  {
    setState(() {
      todoL.removeWhere((item) => item.id==id);
    });

  }

  void _addTodoItem(String text)
  {
    setState(() {
      todoL.add(Todo(id: DateTime.now().microsecondsSinceEpoch.toString(),
          todoText: text));
    });
  _todoController.clear();
  }

  void _runFilter(String searchitem)
  {
    List<Todo> results=[];
    if(searchitem.isEmpty)
      {
        results=todoL;
      }
    else
      {
        results=todoL
            .where((item)=> item.todoText!
              .toLowerCase()
                .contains(searchitem.toLowerCase()))
            .toList();
      }
    setState(() {
      _foundTodo=results;
    });
  }


  Widget searchBox()
  {
    return  Container(
      padding: const EdgeInsets.symmetric(horizontal: 15),
        decoration: BoxDecoration(

          color: Colors.white,
          borderRadius: BorderRadius.circular(20),
        ),
        child:  TextField(
          onChanged: (value)=> _runFilter(value),
          decoration: InputDecoration(
            hintText: 'search',
            contentPadding: EdgeInsets.all(10.0),
            prefixIcon: Icon
              (Icons.search,
              color: Colors.black,
              size: 20,
            ),
            prefixIconConstraints: BoxConstraints(

              maxHeight: 20,
              minWidth: 25,
            ),
          ),
        ),
    );
  }

  AppBar buildAppBar() {
    return AppBar(
      backgroundColor: Colors.purpleAccent,

      title: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children:[

        const Icon(
          Icons.menu,
          color: Colors.black,
          size: 30,
        ),
        Text('ANKAN'),
        Container(
          height: 40,
          width: 40,
          child: ClipRRect(
            borderRadius: BorderRadius.circular(20),
              child: Image.asset('assets/images/img.png'),
          ),
        )
      ]),
      );
  }
}