package rec;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {

    static RootedTree parseRootedTree(String path, String filename) {
        RootedTree tree = new RootedTree();

        try (Scanner s = new Scanner(new File(path + "/" + filename))) {
            String str = s.nextLine();
            //suradnica ciarky (prvy inner node)
            int i = 1;
            int counter = 0;
            while (!(str.charAt(i) == ',' && counter == 0)) {
                if (str.charAt(i) == '(') counter++;
                if (str.charAt(i) == ')') counter--;
                i++;
            }
            //suradnica poslednej zatvorky
            int k = str.length() - 1;
            while (!(str.charAt(k) == ')')) k--;
            //root depth
            Double depth = Double.parseDouble(str.substring(k + 2, str.length() - 1));
            //nastavenie depth pre root
            tree.getRoot().setDepth(depth);
            //nodes pod root
            RootedNode left = parseNode(str.substring(1, i), depth);
            RootedNode right = parseNode(str.substring(i + 1, k), depth);
            //nastaví hierarchiu
            left.setParent(tree.getRoot());
            right.setParent(tree.getRoot());
            tree.getRoot().setLeft(left);
            tree.getRoot().setRight(right);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return tree;
    }

    private static RootedNode parseNode(String str, double parentDepth) {
        RootedNode node;
        if (str.startsWith("(")) {
            //ulozenie inner nodes
            int i = 1;
            int counter = 0;
            int ciarkaIndex = 0;
            while (!(str.charAt(i) == ')' && counter == 0)) {
                if (str.charAt(i) == '(') counter++;
                if (str.charAt(i) == ')') counter--;
                if (str.charAt(i) == ',' && counter == 0) ciarkaIndex = i;
                i++;
            }
            int koniecIndex = i;
            if (koniecIndex == str.length() - 1) {
                System.err.println("Wrong format inner");
                return null;
            }
            i++;
            //vzdialenost od najblizsieho node
            Double depth = Double.parseDouble(str.substring(i + 1));
            //celkova vzdialenost od root
            Double totalDepth = parentDepth + depth;
            //nodes pod
            RootedNode left = parseNode(str.substring(1, ciarkaIndex), totalDepth);
            RootedNode right = parseNode(str.substring(ciarkaIndex + 1, koniecIndex), totalDepth);

            //ulozi nazov nodu v spravnom poradi (napr. S1S2)
            String name;
            if (left.getName().compareTo(right.getName()) < 0) {
                name = left.getName() + right.getName();
            } else {
                name = right.getName() + left.getName();
            }

            //nastavi hierarchiu
            node = new RootedNode(name, totalDepth);
            left.setParent(node);
            right.setParent(node);
            node.setLeft(left);
            node.setRight(right);
            return node;
        } else {
            //ulozenie leaves
            int colonIndex = str.lastIndexOf(':');
            String name;
            double depth;
            if (colonIndex == -1) {
                System.err.println("Wrong format leaf");
                return null;
            } else {
                name = str.substring(0, colonIndex);
                depth = Double.parseDouble(str.substring(colonIndex + 1));
            }
            node = new RootedNode(name, depth + parentDepth);
            return node;
        }
    }
}
