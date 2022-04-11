


public class Todo {

    public String todo;
    public String status;
    
    public Todo(String todo,String status ){
        
        this.todo = todo;
        this.status = status;
    }


    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
