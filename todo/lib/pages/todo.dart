import 'package:flutter/material.dart';
import 'package:todo/listModel/toDo.dart';

class ToDoList extends StatelessWidget{
  final Todo todo;
  final onToDoChange;
  final onDelete;

  ToDoList({super.key, required this.todo, this.onToDoChange, this.onDelete});

  @override
  Widget build(BuildContext context)
  {
    return Container(
      margin: EdgeInsets.only(bottom: 20),

      child: ListTile(
        onTap: () {
          //print('Delete');
          onToDoChange(todo);
        },
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(20),
        ),
        contentPadding: const EdgeInsets.symmetric(
            horizontal: 20,
            vertical: 5,
        ),
        tileColor: Colors.purpleAccent,
        leading:  Icon(
          todo.check? Icons.check_box: Icons.check_box_outline_blank_outlined,
          color: Colors.white,
        ),
        title:  Text(
          todo.todoText!,
          style: TextStyle(
            fontSize: 16,
            color: Colors.black,
            decoration: todo.check? TextDecoration.lineThrough :null,
          ),
        ),
        trailing: Container(
          height: 35,
          width: 35,
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(5),
          ),
          child: IconButton(
            color: Colors.black,
            iconSize: 25,
            icon: const Icon(Icons.delete ),
            onPressed: () {
              onDelete(todo.id);
            },
          ),
        ),
      ),

    );
  }
}