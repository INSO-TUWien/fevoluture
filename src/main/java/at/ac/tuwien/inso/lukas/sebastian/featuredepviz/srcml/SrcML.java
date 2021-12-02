package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.srcml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class SrcML {

    public String getASTAsStringByFileContent(String fileContent, String language) throws IOException {
        return executeCommand(new String[]{
                "srcml",
                "--position",
                "-l",
                language,
                "--src-encoding",
                "utf-8",
                "-text",
                URLEncoder.encode(fileContent, "utf-8")
        });
    }

    public Document generateASTAsXMLFile(String filePath) throws ParserConfigurationException, IOException, SAXException {
        String bufferFileName = "ast.xml";
        getASTByPath(filePath + " --position -o " + bufferFileName);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(bufferFileName);
    }

    public String generateASTAsXMLString(String filePath) throws IOException {
        return getASTByPath(filePath + " --position");
    }

    public List<FileMethod> getFileMethods(Document xmlDoc, String sourceFilePath) throws XPathExpressionException {

        LinkedList<FileMethod> fileMethods = new LinkedList<>();
        XPath xPath = getXpath();
        NodeList nodes = (NodeList) xPath.evaluate("//function", xmlDoc, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node functionNode = nodes.item(i);
            String functionName;
            int functionStartLine = getStartLine(functionNode);
            int functionEndLine = getEndLine(functionNode);

            for (int j = 0; j < functionNode.getChildNodes().getLength(); j++) {
                Node childNode = functionNode.getChildNodes().item(j);

                if (childNode.getNodeName().equals("name")) {
                    functionName = childNode.getTextContent();
                    fileMethods.add(new FileMethod(sourceFilePath, functionName, functionStartLine, functionEndLine));
                }

            }
        }
        return fileMethods;
    }

    public List<FileMethod> getFileMethods(String xmlString, String sourceFilePath) throws XPathExpressionException {
        LinkedList<FileMethod> fileMethods = new LinkedList<>();
        XPath xPath = getXpath();
        InputSource inputSource = new InputSource(new StringReader(xmlString));
        // Use local-name()= because of namespace issue
        NodeList nodes = (NodeList) xPath.evaluate("//*[local-name()='function']", inputSource, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node functionNode = nodes.item(i);
            String functionName;
            int functionStartLine = getStartLine(functionNode);
            int functionEndLine = getEndLine(functionNode);

            for (int j = 0; j < functionNode.getChildNodes().getLength(); j++) {
                Node childNode = functionNode.getChildNodes().item(j);

                if (childNode.getNodeName().equals("name")) {
                    functionName = childNode.getTextContent();
                    FileMethod fileMethod = new FileMethod(sourceFilePath, functionName, functionStartLine, functionEndLine);
                    fileMethods.add(fileMethod);
                }
            }
        }

        return fileMethods;
    }

    public FilePackage getFilePackage(String xmlString, String sourceFilePath) throws XPathExpressionException, TransformerException {
        XPath xPath = getXpath();
        InputSource inputSource = new InputSource(new StringReader(xmlString));
        // Use local-name()= because of namespace issue
        Node node = (Node) xPath.evaluate("//*[local-name()='package']", inputSource, XPathConstants.NODE);

        if (node == null) {
            return null;
        }

        String packageName = null;
        for (int j = 0; j < node.getChildNodes().getLength(); j++) {
            Node childNode = node.getChildNodes().item(j);
            if (childNode.getNodeName().equals("name")) {
                packageName = childNode.getTextContent();
            }
        }

        return new FilePackage(sourceFilePath, packageName);
    }

    private XPath getXpath() {
        return XPathFactory.newInstance().newXPath();
    }

    private String getASTByPath(String filePath) {
        Path path = Paths.get(filePath);
        return executeCommand("srcml " + path);
    }

    private int getStartLine(Node node) {
        String pos = String.valueOf(node.getAttributes().getNamedItem("pos:start"))
                .replace("pos:start=", "")
                .replace("\"", "");
        return Integer.parseInt(pos.substring(0, pos.indexOf(":")));
    }

    private int getEndLine(Node node) {
        String pos = String.valueOf(node.getAttributes().getNamedItem("pos:end"))
                .replace("pos:end=", "")
                .replace("\"", "");
        return Integer.parseInt(pos.substring(0, pos.indexOf(":")));
    }

    private String executeCommand(String command) {
        String text;
        StringBuilder builder = new StringBuilder();
        BufferedReader in;
        try {
            Process p = Runtime.getRuntime().exec(command);
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((text = in.readLine()) != null) {
                builder.append(text);
            }
            p.getInputStream().close();
            p.destroy();
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    private String executeCommand(String[] command) throws IOException {
        String text;
        StringBuilder builder = new StringBuilder();
        BufferedReader in;

        Process p = Runtime.getRuntime().exec(command);
        in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((text = in.readLine()) != null) {
            builder.append(text);
        }
        p.getInputStream().close();
        p.destroy();
        return builder.toString();
    }
}
