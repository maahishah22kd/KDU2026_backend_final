//Create task constructor
function Task(title, priority) {
this.id = Date.now();
this.title = title;
this.priority = priority;
this.completed = false;
}

//Adding methods to Taskprototype
Task.prototype.markComplete= function(){
    this.completed=true;
    return this;
}

Task.prototype.updatePriority= function(newPriority){
    const allowed=["low", "medium", "high"];
    if(!allowed.includes(newPriority)){
        throw new Error("Invalid Priority");
    }
    this.priority=newPriority;
    return this;
}


//Create PriorityTask constructor
function PriorityTask(title,priority,dueDate){
    Task.call(this, title, priority);
    this.dueDate = dueDate;
}
PriorityTask.prototype= Object.create(Task.prototype);
PriorityTask.prototype.constructor = PriorityTask;

Task.prototype.getInfo = function () {
  return `${this.title} [${this.priority}]`;
};

PriorityTask.prototype.getInfo=function(){
    const baseInfo = Task.prototype.getInfo.call(this);

  if (this.dueDate) {
    return `${baseInfo} (Due: ${this.dueDate})`;
  }

  return baseInfo;
}


//Add Utility Method
Task.prototype.getAllTasksInfo = function(tasks){
    return tasks.map(task => task.getInfo());
}


// ----- TESTS -----
const t1 = new Task("Read docs", "low");
const t2 = new Task("Build feature", "medium");

const p1 = new PriorityTask("Submit report", "high", "2026-02-01");
const p2 = new PriorityTask("Pay bills", "low"); 

t1.updatePriority("high").markComplete();
console.log("t1:", t1.getInfo(), "completed:", t1.completed);

try {
  t2.updatePriority("urgent");
} catch (e) {
  console.log("Invalid priority test passed:", e.message);
}

console.log("p1 info:", p1.getInfo()); 
console.log("p2 info:", p2.getInfo()); 

console.log("p1 instanceof PriorityTask:", p1 instanceof PriorityTask); 
console.log("p1 instanceof Task:", p1 instanceof Task); 

const tasks = [t1, t2, p1, p2];
console.log("All tasks info:", t1.getAllTasksInfo(tasks));



//Event Loop & Async Operations
//Create Task with Delay
function createTaskAsync(title, priority){
    console.log("Creating tasks...")

    return new Promise((resolve,reject)=>{
        setTimeout(()=>{
    try {
        if (!title) {
          throw new Error("Title is required");
        }
        const allowed = ["low", "medium", "high"];
        if (!allowed.includes(priority)) {
          throw new Error("Invalid priority");
        }
        const task = new Task(title, priority);
        console.log("Task created!");
        resolve(task);
      } catch (err) {
        reject(err);
      }
    },1000);       
    })
}

//eventloop
function delay(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}
function demonstrateEventLoop() {
  console.log(1);
  delay(2000)
    .then(() => {
      console.log(4);
      return delay(2000);
    })
    .then(() => {
      console.log(3);
      return delay(2000);
    })
    .then(() => {
      console.log(2);
    });
}
demonstrateEventLoop();


//async operation
async function createAndSaveTask(title, priority) {
  try {
    const task1 = await createTaskAsync(title, priority);
    const task2 = await createTaskAsync(
      `${title} (saved)`,
      priority
    );
    console.log("Task created and saved successfully!");
    console.log(task1);
    console.log(task2);
    return task2;
  } catch (err) {
    console.error("Error creating or saving task:", err.message);
    throw err;
  }
}
createAndSaveTask("maahi", "hi")
  .then(task => console.log("Saved:", task))
  .catch(err => console.error("Caller handled:", err.message));

 

  //batch task creation
 function createMultipleTasksAsync(taskDataArray){
    const taskPromises= taskDataArray.map(task=>
        createTaskAsync(task.title, task.priority)
    );

    return Promise.all(taskPromises)
    .then(tasks=>{
        console.log("All tasks created!");
      return tasks;
    })
 }

 createMultipleTasksAsync([
  { title: "Read", priority: "low" },
  { title: "Write", priority: "medium" },
  { title: "Deploy", priority: "high" }
])
.then(tasks => {
  console.log("Returned tasks:", tasks);
})
.catch(err => {
  console.error("Error:", err.message);
});
