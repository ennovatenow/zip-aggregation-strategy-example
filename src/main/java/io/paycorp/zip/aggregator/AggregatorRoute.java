package io.paycorp.zip.aggregator;

import java.io.File;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.zipfile.ZipAggregationStrategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AggregatorRoute extends RouteBuilder {

    @Value("${source.folder:work}")
    private String folder;

    @Value("${delay:10s}")
    private String delay;

    @Override
    public void configure() throws Exception {

        fromF("file:%s?doneFileName=${file:name}.done&delay=%s&includeExt=xml,XML", folder, delay)
                .routeId("fdrl.sftp.mms.xml.route")
                .log(LoggingLevel.INFO,
                        "File name is: ${header.CamelFileParent} | ${header.CamelFilePath} | ${header.CamelFileNameOnly} ")
                .setHeader("ZipFileName", simple("${header.CamelFileName}.zip"))
                .process(new AggregatorProcessor())
                .split(body())
                .aggregationStrategy(new ZipAggregationStrategy())
                .log(LoggingLevel.INFO,
                        "Bytes to be zipped. ${header.CamelFileName}. ${header.ZipFileName}")
                // .setHeader("CamelFileName", simple("${body.getName}"))
                .log(LoggingLevel.INFO, "Part File Name. ${header.CamelFileName}")
                // .convertBodyTo(byte[].class)
                .end()
                .log(LoggingLevel.INFO, "All zipped. ${header.CamelFileName} ${header.ZipFileName} ")
                .setHeader("CamelFileName", simple("${header.ZipFileName}"))
                .toF("file:%s", "work/target");
    }

}
