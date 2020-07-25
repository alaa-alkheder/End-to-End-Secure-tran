const fs = require('fs');
const readline = require('readline');
const {google} = require('googleapis');
 const keys = require('./keys.json');

 const Client=new google.auth.JWT (
     keys.client_email ,
     null ,
     keys.private_key,
     ['https://www.googleapis.com/auth/spreadsheets']);

     Client.authorize(function(err,tokens){
if(err)
{
    console.log(err);
    return;
}
else
console.log("connected!!");

     });


     async function gsrun(cl) {
         const ra='data!A30:A30';
            const gsapi=google.sheets({version:'v4',auth:cl});
            const opt= {
                spreadsheetId:'1hB3I0ushX_AHDv2eZ4yq8WRXLGcUAEpwG4rt8ZgAfB8',
                range:ra
            };

         let data = await gsapi.spreadsheets.values.get(opt);
         console.log(data.data.values);
          let temp =data.data.values;
          console.log(temp[0][0]);
          temp[0][0]=parseInt(temp[0][0]) +1;
          temp[0][0]= ''+temp[0][0] ;
        
          
         console.log(temp);
         const optUdate= {
            spreadsheetId:'1hB3I0ushX_AHDv2eZ4yq8WRXLGcUAEpwG4rt8ZgAfB8',
            range:ra,
            valueInputOption:'USER_ENTERED',
            resource:{values:temp}
        };
         
        let res=await gsapi.spreadsheets.values.update(optUdate);

     } 



     const express = require('express')
const app = express()
const port = 8000;

app.get('/',async (req, res) => {
    gsrun(Client);
    res.send();
})

app.listen(port, () => console.log(`Example app listening at http://localhost:${port}`))