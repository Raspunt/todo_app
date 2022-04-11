<template>

<div class="container">
    
    
    <h2>Todo App</h2>


    <div class="d-flex">
        <input v-model="task" type="text" placeholder="Enter task" class="form-control">
        <button @click="submitTask()" class="btn btn-warning rounded-0">submit</button>
    </div>

    <table class="table table-bordered">
  <thead>
    <tr>
      <th scope="col">Task</th>
      <th scope="col">Status</th>
      <th scope="col">Last</th>
      <th scope="col">Handle</th>
    </tr>
  </thead>
  <tbody>
    <tr v-for="(task,index) in tasks" :key="index">
      <th  :class="{'finished':task.status === 'finished'}">
          {{task.name}}
      </th>

      <td>
          <span @click="changeStatus(index)"> 
              {{task.status}} 
          </span>
      </td>

      <td>
          <div class="text-center" @click="editTask(index)">
              <span class="fa fa-pen"></span>
          </div>
      </td>
      <td>
          <div class="text-center" @click="deleteTask(index)">
              <span class="fa fa-trash"></span>
          </div>
      </td>
    </tr>
   
  </tbody>
</table>

</div>
    
</template>




<script>
export default {
    name:"TodoApp",
    
    data(){
        return {
            tasks:[],
            task:"",
            editedTask:null,
            avalibleStatuses:['to-do',"in-progress","finished"],
            sendOnStart:this.GetTodos()

        }
    },

    methods:{

        submitTask(){
            if(this.task.length === 0) return;

            if (this.editedTask === null){

                this.tasks.push({
                    name:this.task,
                    status:'to-do',
                })

                this.AddNewTodoInDb()
            }
            else
            {
                this.tasks[this.editedTask].name = this.task;
                this.editedTask = null;
            }

            this.task = "";

        },
        deleteTask(index){
            this.tasks.splice(index,1)
        },

        editTask(index){
            this.task = this.tasks[index].name;
            this.editedTask = index
        },

        changeStatus(index){
            let newIndex = this.avalibleStatuses.indexOf(this.tasks[index].status);
            if(++newIndex > 2) newIndex = 0;
            this.tasks[index].status = this.avalibleStatuses[newIndex];

        },

        GetTodos(){

            fetch('http://127.0.0.1:7575/getTodos', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json'
                },
            
            })
            .then(res => res.json())
            .then(
                (res)  => {

                    for(let i = 0;i<res.length; i++){
                        this.tasks.push({
                        name:res[i]["todo"],
                        status:res[i]["status"]
                        })
                    }       
                }    
            );
        },


        AddNewTodoInDb(){

            fetch('http://127.0.0.1:7575/insertTodo', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                
                mode: 'cors',
                cache: 'default',

                body: JSON.stringify({
                    todo:this.task,
                    status:"to-do" 
                })
            
            })

            // let xhr = new XMLHttpRequest();
            // xhr.open("POST", "http://127.0.0.1:7575/insertTodo");

            // xhr.setRequestHeader("Accept", "application/json");
            // xhr.setRequestHeader("Content-Type", "application/json");

            // xhr.onload = () => console.log(xhr.responseText);

            // let data = `{
            // "Id": 78912,
            // "Customer": "Jason Sweet",
            // }`;

            // xhr.send(data);

                    
            }


    }

}
</script>



<style>


.finished{
    text-decoration:line-through ;
}

</style>