package cnsa.demo.service.util;

public class HtmlSymbolConverter {
    public static String getConverted(String input) {
        String convertContent = input;
        convertContent = convertContent.replaceAll("&", "&amp;");
        convertContent = convertContent.replaceAll(" ", "&nbsp;");
        convertContent = convertContent.replaceAll("<", "&lt;");
        convertContent = convertContent.replaceAll(">", "&gt;");
        convertContent = convertContent.replaceAll("\n", "<br>");
        convertContent = convertContent.replaceAll("\"", "&quot;");

        return convertContent;
    }
}
