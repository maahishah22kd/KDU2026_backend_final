const {writeReport}=require('../src/reportGenerator');

function generateSummaryReport(employees, outputPath){
    const totalEmployees = Array.isArray(employees) ? employees.length : 0;
    let totalCompanySalary=0;
    const deptStats={};

    for(const emp of employees){
        const salary= Number(emp.salary)||0;
        totalCompanySalary+=salary;

        const dept=emp.department||'Unknown';
        if(!deptStats[dept]){
            deptStats[dept]={count:0, totalSalary:0};
        }

        deptStats[dept].count+=1;
        deptStats[dept].totalSalary+=salary;
    }

    const avgCompanySalary = totalEmployees === 0 ? 0 : totalCompanySalary / totalEmployees;

    let report = '';
  report += '========================\n';
  report += '      SUMMARY REPORT     \n';
  report += '========================\n\n';

  report += `Total Employees: ${totalEmployees}\n`;
  report += `Total Company Salary: ${totalCompanySalary}\n`;
  report += `Average Salary: ${avgCompanySalary.toFixed(2)}\n\n`;

  report += '------------------------\n';
  report += ' Department Breakdown\n';
  report += '------------------------\n';

  for (const dept of Object.keys(deptStats)) {
    const deptCount = deptStats[dept].count;
    const deptTotal = deptStats[dept].totalSalary;
    const deptAvg = deptCount === 0 ? 0 : deptTotal / deptCount;

    report += `\nDepartment: ${dept}\n`;
    report += `Employees: ${deptCount}\n`;
    report += `Total Salary: ${deptTotal}\n`;
    report += `Average Salary: ${deptAvg.toFixed(2)}\n`;
  }

  report += '\n';

  writeReport(outputPath, report);
}

module.exports = {
  generateSummaryReport
};
