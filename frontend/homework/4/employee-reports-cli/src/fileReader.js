const fs= require('node:fs');
const path=require('node:path');

function readEmployeeData(filepath){
    const absolutePath= path.resolve(filepath);
    const fileContent= fs.readFileSync(absolutePath,'utf-8');
    const employees= JSON.parse(fileContent);
    return employees;
}
module.exports={
    readEmployeeData
};