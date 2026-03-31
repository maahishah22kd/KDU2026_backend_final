const {writeReport}=require('../src/reportGenerator');

function generateDepartmentReport(employees, department, outputPath){
    const list = Array.isArray(employees) ? employees : [];
    const targetDept = (department || '').trim();

    const deptEmployees = list.filter(emp => {
    const dept = (emp.department || '').trim();
    return dept.toLowerCase() === targetDept.toLowerCase();
  });

  const deptCount = deptEmployees.length
  let deptTotalSalary = 0;
  for (const emp of deptEmployees) {
    deptTotalSalary += Number(emp.salary) || 0;
  }

  const deptAvgSalary = deptCount === 0 ? 0 : deptTotalSalary / deptCount;
  let report = '';
  report += '========================\n';
  report += '   DEPARTMENT REPORT     \n';
  report += '========================\n\n';

  report += `Department: ${targetDept}\n`;
  report += `Number of Employees: ${deptCount}\n`;
  report += `Total Salary: ${deptTotalSalary}\n`;
  report += `Average Salary: ${deptAvgSalary.toFixed(2)}\n\n`;

  report += '------------------------\n';
  report += ' Employees (Name | Salary)\n';
  report += '------------------------\n';

  if (deptCount === 0) {
    report += '\nNo employees found for this department.\n';
  } else {
    for (const emp of deptEmployees) {
      report += `${emp.name} | ${emp.salary}\n`;
    }
  }

  report += '\n';
  writeReport(outputPath, report);
}

module.exports = {
  generateDepartmentReport
};
