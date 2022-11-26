package io.paycorp.zip.aggregator;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.file.FileConsumer;
import org.apache.camel.component.file.GenericFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AggregatorProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        var fileName = exchange.getIn().getHeader(Exchange.FILE_NAME_ONLY, String.class);
        var filePath = exchange.getIn().getHeader(Exchange.FILE_PARENT, String.class);

        Collection<GenericFile<File>> list = new ArrayList<GenericFile<File>>(3);

        list.add(FileConsumer.asGenericFile(filePath, Paths.get(filePath, fileName + ".01").toFile(),
                StandardCharsets.UTF_8.name(), false));
        list.add(FileConsumer.asGenericFile(filePath, Paths.get(filePath, fileName + ".02").toFile(),
                StandardCharsets.UTF_8.name(), false));

        log.info("List is: {}", list.size());
        exchange.getIn().setBody(list);
    }

}
