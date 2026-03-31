const fs = require('node:fs');
const path = require('node:path');

function writeReport(filePath, content){
    const directoryPath= path.dirname(filePath);
    if (!fs.existsSync(directoryPath)) {
        fs.mkdirSync(directoryPath, { recursive: true });
    }
    fs.writeFileSync(filePath, content, 'utf-8');
}

module.exports={
    writeReport
};