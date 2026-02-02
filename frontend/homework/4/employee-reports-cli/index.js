const { readEmployeeData } = require('./src/fileReader');
const { generateSummaryReport } = require('./reports/summaryReport');
const { generateDepartmentReport } = require('./reports/departmentReport');
const { generateTopEarnersReport } = require('./reports/topEarnersReport');

function main() {
  const employees = readEmployeeData('./data/employees.json');

  const command = (process.argv[2] || '').toLowerCase();

  const defaultDepartment = 'Engineering';
  const defaultTopCount = 3;

  if (command==='summary') {
    generateSummaryReport(employees, './reports/summary.txt');
    return;
  }

  if (command==='department') {
    const deptArg = process.argv[3] || defaultDepartment;
    generateDepartmentReport(employees, deptArg, './reports/department.txt');
    return;
  }

  if (command==='top') {
    const countArg = Number(process.argv[3] || defaultTopCount);
    const safeCount = Number.isFinite(countArg) && countArg > 0 ? countArg : defaultTopCount;

    generateTopEarnersReport(employees, safeCount, './reports/rank.txt');
    return;
  }

  generateSummaryReport(employees, './reports/summary.txt');
  generateDepartmentReport(employees, defaultDepartment, './reports/department.txt');
  generateTopEarnersReport(employees, defaultTopCount, './reports/rank.txt');

  if (command) {
    console.log('\nUnknown command:', command);
    console.log('Usage:');
    console.log('  node index.js summary');
    console.log('  node index.js department [DepartmentName]');
    console.log('  node index.js top [Count]');
    console.log('  node index.js   (no args = all reports)\n');
  }
}

main();
