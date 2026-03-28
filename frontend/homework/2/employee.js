//Create employees
const employee1 = {
  id: 1,
  name: "Maahi Shah",
  age: 21,
  salary: 85000,
  department: "Engineering",
  skills: ["JavaScript", "React", "Node.js"],
  experience: 5
};

const employee2 = {
  id: 2,
  name: "Keertana",
  age: 28,
  salary: 65000,
  department: "Marketing",
  skills: ["SEO", "Content Writing", "JavaScript"],
  experience: 3
};

const employee3 = {
  id: 3,
  name: "Suhani",
  age: 35,
  salary: 95000,
  department: "Engineering",
  skills: ["Java", "Spring", "SQL"],
  experience: 8
};

const employee4 = {
  id: 4,
  name: "Shlok Patel",
  age: 40,
  salary: 105000,
  department: "Management",
  skills: ["Leadership", "Budgeting", "Strategy"],
  experience: 12
};

const employee5 = {
  id: 5,
  name: "Shaiva",
  age: 26,
  salary: 60000,
  department: "Design",
  skills: ["UI/UX", "Figma", "Adobe XD"],
  experience: 2
};

//Get employee info
function getEmployeeInfo(employee){
    return `${employee.name} works in ${employee.department} and earns ${employee.salary}`;
}
console.log("Employee info: "+getEmployeeInfo(employee1));

//Add a skill
function addSkill(employee,skill){
    employee.skills = [...employee.skills, skill];
}
addSkill(employee1,"Typescript");
console.log("Added skill:" + employee1.skills);

//Compare employees
function compareEmployees(emp1, emp2) {
  if (emp1.skills.length > emp2.skills.length) {
    return emp1.name;
  } else if (emp2.skills.length > emp1.skills.length) {
    return emp2.name;
  } else {
    return "Both employees have the same number of skills";
  }
}
console.log("Compare employees:" +compareEmployees(employee1, employee2));

//Get all employees info
function getFullInfo(){
  return`
ID: ${this.id}
Name: ${this.name}
Age: ${this.age}
Department: ${this.department}
Salary: ${this.salary}
Experience: ${this.experience} years
Skills: ${this.skills.join(", ")}
`.trim();
}
employee1.getFullInfo = getFullInfo;
employee2.getFullInfo = getFullInfo;
employee3.getFullInfo = getFullInfo;
employee4.getFullInfo = getFullInfo;
employee5.getFullInfo = getFullInfo;

console.log("Get full info: "+employee1.getFullInfo());
console.log("Get full info: "+employee2.getFullInfo());
console.log("Get full info: "+employee3.getFullInfo());
console.log("Get full info: "+employee4.getFullInfo());
console.log("Get full info: "+employee5.getFullInfo());


//Convert to array
const employees = [employee1, employee2, employee3, employee4, employee5];
console.log(employees);


//filter
function filterByExperience(employees, minExperience) {
  return employees.filter(employee => employee.experience >= minExperience);
}
console.log(`Filtered array:`);
console.log(filterByExperience(employees,5));


//Map
function mapDepartmentToSalary(employees){
    return employees.map(employee => 
    `${employee.name}(${employee.department}) - ${employee.salary}`    
    );
}
console.log(mapDepartmentToSalary(employees));


//Average calc
function calculateAverageSalary(employees) {
  const totalSalary = employees.reduce((sum, employee) => {
    return sum + employee.salary;
  }, 0);

  return totalSalary / employees.length;
}
console.log("Avergae salary: "+calculateAverageSalary(employees));


//Department wise count
function countEmployeesByDepartment(employees) {
  return employees.reduce((acc, employee) => {
    acc[employee.department]= (acc[employee.department] || 0)+1;
    return acc;
  }, {});
}
console.log("Department-wise employee count");
console.log(countEmployeesByDepartment(employees));


//Highest paid employee
function findHighestPaidEmployee(employees) {
  return employees.reduce((highest, employee) => {
    return employee.salary > highest.salary ? employee : highest;
  });
}
console.log("Highest paid employee:")
console.log(findHighestPaidEmployee(employees));

//sort by experience
function sortByExperience(employees){
  return [...employees].sort((a,b)=> b.experience-a.experience);
}
console.log("Sorted by sexperience descending");
console.log(sortByExperience(employees));


//Destructuring
function getDetails(employee){
  const {name,salary,department}=employee;
  return `${name} works in ${department} and earns ${salary}`;
}
console.log("Employee info: "+getDetails(employee1));


//array destructuring
function getTopAndBottomPaidEmployees(employees){
  const sorted=[...employees].sort((a,b)=> b.salary-a.salary);
  const [topPaid, , , ,bottomPaid]= sorted;
  return {topPaid,bottomPaid};
}
console.log("Top and Bottom paid emp:");
console.log(getTopAndBottomPaidEmployees(employees));


//merge and remove duplicates
function mergeSkills(emp1, emp2){
   return [...new Set([...emp1.skills, ...emp2.skills])];
}
console.log("Merged skills:");
console.log(mergeSkills(employee1,employee2));


//Rest operator
function getEmployeeStats(...employees){
  const totalEmployees= employees.length;
  const totalAge = employees.reduce((age,employee)=>{
    return age+employee.age;
  },0);

  const avgAge = totalAge===0?0: totalAge/totalEmployees;
  return {
    totalEmployees, avgAge
  };
}
console.log("Employee stats:");
console.log(getEmployeeStats(employee1,employee2,employee3));



//Integration 
function getAnalytics(employees){
  return employees.reduce((acc,employee)=>{
    employee.skills.forEach(skill => {
      acc[skill]= (acc[skill]||0)+1;
    });
    return acc;
  },{});
}
console.log("Skill-wise count");
console.log(getAnalytics(employees));
