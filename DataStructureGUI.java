package teamproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class DataStructureGUI extends JFrame {

    public DataStructureGUI() {
        setTitle("Data Structure Visualizer");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Queue button in the main frame
        JButton queueButton = new JButton("Queue");
        queueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openOperationsFrame("Queue");
            }
        });

        // Stack button in the main frame
        JButton stackButton = new JButton("Stack");
        stackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openOperationsFrame("Stack");
            }
        });

        // Add buttons to the main frame
        add(queueButton);
        add(stackButton);

        setVisible(true);
    }

    // Method to open the operations frame with Insert and Delete buttons
    private void openOperationsFrame(String dataStructureType) {
        JFrame operationsFrame = new JFrame(dataStructureType + " Operations");
        operationsFrame.setSize(400, 300);
        operationsFrame.setLayout(new FlowLayout());
        operationsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Label to show the current state of the structure
        JLabel displayLabel = new JLabel(dataStructureType + " Elements: ");
        operationsFrame.add(displayLabel);

        // LinkedList to represent Queue or Stack
        LinkedList<Integer> dataStructure = new LinkedList<>();

        // Insert button for adding elements
        JButton insertButton = new JButton("Insert");
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add element and update display based on the type
                int element = (int) (Math.random() * 100); // Random element for demo
                if (dataStructureType.equals("Queue")) {
                    dataStructure.addLast(element); // Queue: Add to the end
                } else {
                    dataStructure.addFirst(element); // Stack: Add to the front
                }
                updateDisplay(displayLabel, dataStructure, dataStructureType);
            }
        });

        // Delete button for removing elements
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Remove element and update display based on the type
                if (!dataStructure.isEmpty()) {
                    if (dataStructureType.equals("Queue")) {
                        dataStructure.removeFirst(); // Queue: Remove from the front
                    } else {
                        dataStructure.removeFirst(); // Stack: Remove from the front
                    }
                }
                updateDisplay(displayLabel, dataStructure, dataStructureType);
            }
        });

        // Add buttons to the operations frame
        operationsFrame.add(insertButton);
        operationsFrame.add(deleteButton);

        operationsFrame.setVisible(true);
    }

    // Method to update the display label with the current elements in the data structure
    private void updateDisplay(JLabel displayLabel, LinkedList<Integer> dataStructure, String type) {
        StringBuilder displayText = new StringBuilder(type + " Elements: ");
        for (Integer element : dataStructure) {
            displayText.append(element).append(" ");
        }
        displayLabel.setText(displayText.toString());
    }

    public static void main(String[] args) {
        new DataStructureGUI();
    }
}


