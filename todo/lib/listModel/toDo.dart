class Todo{
  String? id;
  String? todoText;
  bool check;

  Todo({
    required this.id,
    required this.todoText,
    this.check=false,
  });

  static List<Todo> todoList(){
    return[
      Todo(id: '01', todoText: 'Login in LeetCode', check: true ),
      Todo(id: '02', todoText: 'Hit the gym', check: true ),
      Todo(id: '03', todoText: 'Check Emails', ),
      Todo(id: '04', todoText: 'work on project', ),
      Todo(id: '05', todoText: 'solve dsa', ),
      Todo(id: '06', todoText: 'do cardio', ),
    ];
  }
}