This example picks files from ./work/source folder and pushes the zip file to the ./work/target folder.
To pick a file make sure you create a done file

AggregatorProcessor - reads files, converts into GenericFile objects using the FileConsumer and puts it in the Exchange body
The route splits the list and aggregates them into a Zip file


