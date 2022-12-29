# Populator

This lambda function is designed to grab the latest APOD and store it into a Mongo DB storage.

This lambda will be triggered once a day by a CloudWatch event.

## Deployment

Currently, there is no pipeline that deploys the lambda function automatically after a code change has been pushed.

You must manually create a zip file and upload the zip file directly in the Lambda console (or via CLI).
- `node_modules`
- `package.json`
- `package-lock.json`
- `index.js`