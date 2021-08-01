package com.example.football.util;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface XmlParser {
    <E> E fromFiles(String filePath, Class<E> eClass) throws JAXBException, FileNotFoundException;
}
