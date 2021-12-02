package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommitUtils {

    /**
     * Extracts a list of all Issue Ids based on a pattern from a commit message.
     *
     * @param commitMessage
     * @param patterns
     * @return
     */
    public static List<String> extractIssueKeys(String commitMessage, String[] patterns) {
        LinkedList<String> keys = new LinkedList<>();
        for (String patternString : patterns) {
            Pattern p = Pattern.compile(patternString);
            Matcher m = p.matcher(commitMessage);
            while (m.find()) {
                String issueKey = m.group();
                keys.add(issueKey);
            }
        }
        return keys;
    }
}
