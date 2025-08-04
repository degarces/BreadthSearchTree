import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class BST extends JFrame {

    private static class Node {
        int value;
        int count;
        Node left, right;

        public Node(int value) {
            this.value = value;
            this.count = 1;
        }
    }

    private Node root;

    // BST Methods

    public void insert(int value) {
        root = insert(root, value);
    }

    private Node insert(Node node, int value) {
        if (node == null) return new Node(value);

        if (value < node.value) {
            node.left = insert(node.left, value);
        } else if (value > node.value) {
            node.right = insert(node.right, value);
        } else {
            node.count++;
        }
        return node;
    }

    public Node search(int value) {
        return search(root, value);
    }

    private Node search(Node node, int value) {
        if (node == null || node.value == value) return node;
        return value < node.value ? search(node.left, value) : search(node.right, value);
    }

    private Node findMax(Node node) {
        if (node.right == null) return node;
        return findMax(node.right);
    }

    private Node findMin(Node node) {
        if (node.left == null) return node;
        return findMin(node.left);
    }

    public void delete(int value) {
        root = delete(root, value);
    }

    private Node delete(Node node, int value) {
        if (node == null) return null;

        if (value < node.value) {
            node.left = delete(node.left, value);
        } else if (value > node.value) {
            node.right = delete(node.right, value);
        } else {
            if (node.count > 1) {
                node.count--;
            } else {
                if (node.left == null && node.right == null) {
                    node = null;
                } else if (node.left == null) {
                    node = node.right;
                } else if (node.right == null) {
                    node = node.left;
                } else {
                    Node minNode = findMin(node.right);
                    node.value = minNode.value;
                    node.count = minNode.count;
                    node.right = deleteMin(node.right);
                }
            }
        }
        return node;
    }

    private Node deleteMin(Node node) {
        if (node.left == null) return node.right;
        node.left = deleteMin(node.left);
        return node;
    }

    // Traversals - return string outputs for display

    private void preorder(Node node, StringBuilder sb) {
        if (node != null) {
            sb.append(node.value).append("(").append(node.count).append(") ");
            preorder(node.left, sb);
            preorder(node.right, sb);
        }
    }

    private void inorder(Node node, StringBuilder sb) {
        if (node != null) {
            inorder(node.left, sb);
            sb.append(node.value).append("(").append(node.count).append(") ");
            inorder(node.right, sb);
        }
    }

    private void postorder(Node node, StringBuilder sb) {
        if (node != null) {
            postorder(node.left, sb);
            postorder(node.right, sb);
            sb.append(node.value).append("(").append(node.count).append(") ");
        }
    }

    private void breadthFirst(StringBuilder sb) {
        if (root == null) return;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            sb.append(current.value).append("(").append(current.count).append(") ");
            if (current.left != null) queue.add(current.left);
            if (current.right != null) queue.add(current.right);
        }
    }

    // GUI Components

    private JTextField inputField;
    private JTextArea outputArea;

    public BST() {
        super("BST GUI");

        // Layout
        setLayout(new BorderLayout(10, 10));
        JPanel inputPanel = new JPanel(new FlowLayout());

        inputField = new JTextField(10);
        JButton insertBtn = new JButton("Insert");
        JButton searchBtn = new JButton("Search");
        JButton deleteBtn = new JButton("Delete");

        inputPanel.add(new JLabel("Value:"));
        inputPanel.add(inputField);
        inputPanel.add(insertBtn);
        inputPanel.add(searchBtn);
        inputPanel.add(deleteBtn);

        add(inputPanel, BorderLayout.NORTH);

        // Traversal buttons
        JPanel traversalPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton preorderBtn = new JButton("Preorder");
        JButton inorderBtn = new JButton("Inorder");
        JButton postorderBtn = new JButton("Postorder");
        JButton breadthFirstBtn = new JButton("Breadth-First");
        JButton findMaxBtn = new JButton("Find Max");
        JButton findMinBtn = new JButton("Find Min");

        traversalPanel.add(preorderBtn);
        traversalPanel.add(inorderBtn);
        traversalPanel.add(postorderBtn);
        traversalPanel.add(breadthFirstBtn);
        traversalPanel.add(findMaxBtn);
        traversalPanel.add(findMinBtn);


        add(traversalPanel, BorderLayout.CENTER);

        // Output area
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.SOUTH);

        // Button Actions

        findMaxBtn.addActionListener(e -> {
            if (root == null) {
                outputArea.setText("Tree is empty.");
            } else {
                Node maxNode = findMax(root);
                outputArea.setText("Max value: " + maxNode.value + "(" + maxNode.count + ")");
            }
        });

        findMinBtn.addActionListener(e -> {
            if (root == null) {
                outputArea.setText("Tree is empty.");
            } else {
                Node minNode = findMin(root);
                outputArea.setText("Min value: " + minNode.value + "(" + minNode.count + ")");
            }
        });
        insertBtn.addActionListener(e -> {
            String text = inputField.getText().trim();
            try {
                int val = Integer.parseInt(text);
                insert(val);
                outputArea.setText("Inserted: " + val);
                inputField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        searchBtn.addActionListener(e -> {
            String text = inputField.getText().trim();
            try {
                int val = Integer.parseInt(text);
                Node node = search(val);
                if (node != null) {
                    outputArea.setText("Found: " + node.value + "(" + node.count + ")");
                } else {
                    outputArea.setText("Value " + val + " not found.");
                }
                inputField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteBtn.addActionListener(e -> {
            String text = inputField.getText().trim();
            try {
                int val = Integer.parseInt(text);
                delete(val);
                outputArea.setText("Deleted: " + val + " (if it existed)");
                inputField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        preorderBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            preorder(root, sb);
            outputArea.setText("Preorder traversal:\n" + sb.toString());
        });

        inorderBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            inorder(root, sb);
            outputArea.setText("Inorder traversal:\n" + sb.toString());
        });

        postorderBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            postorder(root, sb);
            outputArea.setText("Postorder traversal:\n" + sb.toString());
        });

        breadthFirstBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            breadthFirst(sb);
            outputArea.setText("Breadth-First traversal:\n" + sb.toString());
        });

        // JFrame settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Main entry point

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BST::new);
    }
}
