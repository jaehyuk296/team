package teamproject;

// GUI 관련 패키지
import javax.swing.*;
import java.awt.*;
import java.util.Stack;

// 오디오 파일 재생 관련 패키지
import javax.sound.sampled.*;
import java.io.*;

// 메인 GUI 클래스
public class DataStructureGUI extends JFrame {

    public DataStructureGUI() { // 메인 GUI
        setTitle("자료구조"); // 윈도우 제목
        setSize(1000, 708); // 윈도우 크기
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 닫기 버튼 동작

        // 메인 컨테이너의 레이아웃 설정
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        // 배경 이미지를 표시하는 커스텀 패널
        ImagePanel mainPanel = new ImagePanel("main.png");
        mainPanel.setLayout(new BorderLayout());
        c.add(mainPanel, BorderLayout.CENTER);

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setOpaque(false);

        // 스택 버튼 생성 및 설정
        JButton btnStack = new JButton("Stack");
        btnStack.setFont(new Font("고딕", Font.PLAIN, 18));
        btnStack.setPreferredSize(new Dimension(150, 40));

        // 큐 버튼 생성 및 설정
        JButton btnQueue = new JButton("Queue");
        btnQueue.setFont(new Font("고딕", Font.PLAIN, 18));
        btnQueue.setPreferredSize(new Dimension(150, 40));

        // 버튼을 버튼 패널에 추가
        buttonPanel.add(btnStack);
        buttonPanel.add(btnQueue);

        // 하단 패널을 메인 패널의 남쪽에 추가
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 스택 버튼 클릭 이벤트: 스택 윈도우 열기
        btnStack.addActionListener(e -> new StackWindow());

        // 큐 버튼 클릭 이벤트: 큐 윈도우 열기
        btnQueue.addActionListener(e -> new QueueWindow());
        // 메인 GUI 표시
        setVisible(true);
    }

    // 이미지 배경을 지원하는 커스텀 JPanel
    class ImagePanel extends JPanel {
        private Image backgroundImage;

        public ImagePanel(String imagePath) {
            try { // 이미지 로드
                backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "이미지를 로드할 수 없습니다: " + imagePath, "오류", JOptionPane.ERROR_MESSAGE);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (backgroundImage != null) { // 패널 크기에 맞게 이미지를 그리기
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    // 스택 기능을 구현하는 별도의 윈도우
    class StackWindow extends JFrame {
        private final Stack<String> stack = new Stack<>(); // 스택 데이터 구조
        private final JPanel stackPanel; // 스택 데이터를 시각화하는 패널

        private final JLabel leftImageLabel; // 삽입 시 이미지
        private final JLabel rightImageLabel; // 삭제 시 이미지
        private Timer imageTimer; // 이미지 타이머 (자동 제거)

        private boolean isBlinking = false; // 깜빡임 상태
        private int blinkCounter = 0; // 깜빡임 카운터
        private int top = -1; // 스택의 상단 인덱스 초기화

        public StackWindow() {
            setTitle("스택 구현"); // 윈도우 제목
            setSize(700, 600); // 윈도우 크기 설정
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            Container c = getContentPane();
            c.setLayout(new BorderLayout());
            c.setBackground(Color.WHITE);
            UIManager.put("OptionPane.messageFont", new Font("고딕", Font.PLAIN, 15));

            // 이미지 레이블 설정
            leftImageLabel = new JLabel(); // Placeholder for left image
            rightImageLabel = new JLabel(); // Placeholder for right image

            leftImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            rightImageLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // 좌우 이미지 영역 크기 설정
            leftImageLabel.setPreferredSize(new Dimension(220, 400)); // Adjusted image area
            rightImageLabel.setPreferredSize(new Dimension(220, 400));

            // 이미지 레이블을 컨테이너에 추가
            c.add(leftImageLabel, BorderLayout.WEST);  // Add left image
            c.add(rightImageLabel, BorderLayout.EAST); // Add right image

            // 스택 데이터를 표시할 패널
            stackPanel = new JPanel();
            stackPanel.setLayout(new BoxLayout(stackPanel, BoxLayout.Y_AXIS));
            stackPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            c.add(stackPanel, BorderLayout.CENTER);

            // 버튼 생성 및 하단에 추가
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());

            JButton btnInsert = new JButton("삽입");
            btnInsert.setPreferredSize(new Dimension(100, 30));
            btnInsert.setFont(new Font("고딕", Font.PLAIN, 13));

            JButton btnDelete = new JButton("삭제");
            btnDelete.setPreferredSize(new Dimension(100, 30));
            btnDelete.setFont(new Font("고딕", Font.PLAIN, 13));

            buttonPanel.add(btnInsert);
            buttonPanel.add(btnDelete);

            // 삽입 버튼 동작 정의
            btnInsert.addActionListener(e -> {
                if (stack.size() >= 10) {
                    SoundPlayer.playSound("alert.wav"); // 스택이 가득찰때 효과음
                    JOptionPane.showMessageDialog(null, "스택이 가득 찼습니다!", "오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String value = JOptionPane.showInputDialog("삽입할 값을 입력하세요:");
                if (value != null && !value.trim().isEmpty()) {
                    stack.push(value); // 값 삽입
                    top = stack.size() - 1; // 삽입된 값의 인덱스

                    // 삽입 이미지를 명시적으로 숨기기
                    rightImageLabel.setIcon(null);
                    rightImageLabel.revalidate();
                    rightImageLabel.repaint();

                    // 삽입 이미지 표시
                    leftImageLabel.setIcon(new ImageIcon(getClass().getResource("push.jpg")));
                    leftImageLabel.revalidate();
                    leftImageLabel.repaint();
                    startImageTimer(leftImageLabel);

                    // 깜빡임 효과 실행
                    blinkStack();
                }
            });

            // 삭제 버튼 동작 정의
            btnDelete.addActionListener(e -> {
                if (stack.isEmpty()) {
                    SoundPlayer.playSound("alert.wav"); // 스택이 비어 있을 때 경고음 재생
                    JOptionPane.showMessageDialog(null, "스택이 비어 있습니다!", "오류", JOptionPane.ERROR_MESSAGE);
                } else {
                    stack.pop(); // 값 제거
                    top = stack.size() - 1; // TOP 인덱스 갱신
                    updateStackPanel();

                    // 삽입 이미지를 명시적으로 숨기기
                    leftImageLabel.setIcon(null);
                    leftImageLabel.revalidate();
                    leftImageLabel.repaint();

                    // 삭제 이미지 표시
                    rightImageLabel.setIcon(new ImageIcon(getClass().getResource("pop.jpg")));
                    rightImageLabel.revalidate();
                    rightImageLabel.repaint();
                    startImageTimer(rightImageLabel);
                }
            });

            JButton btnSaveStack = new JButton("저장");
            btnSaveStack.setPreferredSize(new Dimension(100, 30));
            btnSaveStack.setFont(new Font("고딕", Font.PLAIN, 13));
            buttonPanel.add(btnSaveStack);
            btnSaveStack.addActionListener(e -> saveStackToFile("stack_state.txt"));

            JButton btnLoadStack = new JButton("불러오기");
            btnLoadStack.setPreferredSize(new Dimension(100, 30));
            btnLoadStack.setFont(new Font("고딕", Font.PLAIN, 13));
            buttonPanel.add(btnLoadStack);
            btnLoadStack.addActionListener(e -> loadStackFromFile("stack_state.txt"));

            buttonPanel.add(btnSaveStack);
            buttonPanel.add(btnLoadStack);

            c.add(buttonPanel, BorderLayout.SOUTH);

            setVisible(true);
        }

        // 스택 상태를 파일에 저장하는 메서드
        private void saveStackToFile(String fileName) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                for (String value : stack) { // Stack의 모든 요소 저장
                    writer.write(value + "\n");
                }
                JOptionPane.showMessageDialog(this, "스택 상태가 저장되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "파일 저장 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // 파일에서 스택 상태를 불러오는 메서드
        private void loadStackFromFile(String fileName) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                stack.clear(); // 스택 초기화
                String line;
                while ((line = reader.readLine()) != null) {
                    stack.push(line); // 파일에서 읽은 데이터를 스택에 삽입
                }
                top = stack.size() - 1; // TOP 인덱스 갱신
                updateStackPanel(); // 스택 시각화 갱신
                JOptionPane.showMessageDialog(this, "스택 상태가 불러와졌습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "파일 불러오기 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // 스택 데이터를 다시 그리는 메서드
        private void updateStackPanel() {
            stackPanel.removeAll(); // 기존 내용 삭제
            stackPanel.setLayout(new BoxLayout(stackPanel, BoxLayout.Y_AXIS));

            // 내용 하단 정렬
            stackPanel.add(Box.createVerticalGlue());

            // 스택 요소 표시
            for (int i = stack.size() - 1; i >= 0; i--) {
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));
                itemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                // TOP 인덱스에 해당하는 경우 "TOP" 라벨 추가
                JLabel topLabel = new JLabel(" ");
                if (i == top) {
                    topLabel = new JLabel("TOP: " + i);
                    topLabel.setFont(new Font("고딕", Font.BOLD, 16));
                    topLabel.setForeground(Color.RED);
                }
                topLabel.setPreferredSize(new Dimension(60, 40)); // 일관된 크기 유지
                topLabel.setMinimumSize(new Dimension(60, 40));
                topLabel.setMaximumSize(new Dimension(60, 40));
                topLabel.setHorizontalAlignment(SwingConstants.LEFT);
                itemPanel.add(topLabel);

                JLabel label = new JLabel(stack.get(i));
                label.setFont(new Font("고딕", Font.PLAIN, 16));
                Dimension labelSize = new Dimension(140, 40); // 라벨 크기 조정
                label.setPreferredSize(labelSize);
                label.setMinimumSize(labelSize);
                label.setMaximumSize(labelSize);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);

                if (i == top && blinkCounter == 1) {
                    label.setBackground(Color.YELLOW);
                } else {
                    label.setBackground(Color.WHITE);
                }

                itemPanel.add(label);
                stackPanel.add(itemPanel);
            }

            stackPanel.revalidate();
            stackPanel.repaint();
        }

        // 이미지 타이머
        private void startImageTimer(JLabel imageLabel) {
            if (imageTimer != null && imageTimer.isRunning()) {
                imageTimer.stop(); // 기존 타이머가 실행 중이면 중지
            }
            imageTimer = new Timer(2000, e -> { // 2000ms (2초 후 실행)
                imageLabel.setIcon(null); // 이미지 숨기기
                imageLabel.revalidate(); // 레이블 레이아웃 다시 계산
                imageLabel.repaint(); // UI 다시 그리기
            });
            imageTimer.setRepeats(false); // 타이머가 한 번만 실행되도록 설정
            imageTimer.start(); // 타이머 시작
        }

        // 반짝임
        private void blinkStack() {
            // 이미 깜빡임 중이거나 깜빡임 대상 인덱스가 없으면 메서드 종료
            if (isBlinking || top == -1) return;
            isBlinking = true; // 깜빡임 상태 활성화

            // 별도의 스레드에서 깜빡임 동작 실행
            Thread blinkThread = new Thread(() -> {
                try {
                    // 총 6번 깜빡임 (3번 점멸)
                    for (int i = 0; i < 6; i++) {
                        blinkCounter = i % 2; // 0과 1로 상태 번갈아 변경
                        updateStackPanel(); // 스택 패널 업데이트 (색상 반영)
                        Thread.sleep(300); // 300ms 간격으로 깜빡임
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    isBlinking = false; // 깜빡임 상태 비활성화
                    blinkCounter = 0; // 깜빡임 상태 초기화
                    updateStackPanel(); // 스택 패널 최종 업데이트
                }
            });
            blinkThread.start(); // 스레드 시작
        }
    }

    // 큐 윈도우
    class QueueWindow extends JFrame {
        private final int MAX_SIZE = 10; // 큐의 최대 크기 (10개 원소)
        private final String[] queue = new String[MAX_SIZE]; // 큐 데이터를 저장할 배열
        private int front = 0; // 큐의 첫 번째 원소 인덱스
        private int rear = 0;  // 큐의 마지막 원소 인덱스

        private final CircleCanvas canvas; // 큐를 시각적으로 표현할 캔버스
        private final JLabel imageLabel;  // 삽입/삭제 이미지 표시용 레이블

        private Timer imageTimer; // 이미지 타이머
        private boolean isBlinking = false; // 깜빡임 여부
        private int blinkCounter = 0; // 깜빡임 상태

        public QueueWindow() {
            setTitle("원형 큐 구현"); // 창 제목 설정
            setSize(600, 600); // 창 크기 설정
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 창 닫기 설정
            UIManager.put("OptionPane.messageFont", new Font("고딕", Font.PLAIN, 15));

            Container c = getContentPane();
            c.setLayout(new BorderLayout()); // BorderLayout으로 창 구성
            c.setBackground(Color.WHITE); // 배경색 설정

            // 하단 버튼 패널 구성
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());

            JButton btnInsert = new JButton("삽입"); // 삽입 버튼
            btnInsert.setFont(new Font("고딕", Font.PLAIN, 18)); // 버튼 글꼴 설정
            btnInsert.setPreferredSize(new Dimension(120, 50)); // 버튼 크기 설정

            JButton btnDelete = new JButton("삭제"); // 삭제 버튼
            btnDelete.setFont(new Font("고딕", Font.PLAIN, 18));
            btnDelete.setPreferredSize(new Dimension(120, 50));

            buttonPanel.add(btnInsert); // 버튼 패널에 삽입 버튼 추가
            buttonPanel.add(btnDelete); // 버튼 패널에 삭제 버튼 추가

            JButton btnSave = new JButton("저장");
            btnSave.setFont(new Font("고딕", Font.PLAIN, 18));
            btnSave.setPreferredSize(new Dimension(120, 50));
            btnSave.addActionListener(e -> saveQueueToFile("queue_state.txt"));

            JButton btnLoad = new JButton("불러오기");
            btnLoad.setFont(new Font("고딕", Font.PLAIN, 18));
            btnLoad.setPreferredSize(new Dimension(120, 50));
            btnLoad.addActionListener(e -> loadQueueFromFile("queue_state.txt"));

            buttonPanel.add(btnSave);
            buttonPanel.add(btnLoad);

            c.add(buttonPanel, BorderLayout.SOUTH); // 버튼 패널을 창 하단에 추가

            // 큐를 표시하는 캔버스
            canvas = new CircleCanvas();
            canvas.setPreferredSize(new Dimension(600, 500));
            c.add(canvas, BorderLayout.CENTER);

            // 삽입/삭제 이미지를 표시할 레이블
            imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER); // 중앙 정렬
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            imageLabel.setOpaque(false); // 투명 배경
            imageLabel.setVisible(false); // 초기에는 보이지 않도록 설정

            // 캔버스와 레이블을 중첩시킬 레이어드 패널
            JLayeredPane layeredPane = new JLayeredPane();
            layeredPane.setLayout(null);
            canvas.setBounds(0, 0, 600, 500);
            imageLabel.setBounds(0, 0, 600, 500);

            layeredPane.add(canvas, Integer.valueOf(0)); // 캔버스 레이어 추가
            layeredPane.add(imageLabel, Integer.valueOf(1)); // 이미지 레이블 레이어 추가
            c.add(layeredPane, BorderLayout.CENTER);

            // 버튼 액션 리스너 추가
            btnInsert.addActionListener(e -> {
                // 큐가 가득 찼는지 확인
                if ((rear + 1) % MAX_SIZE == front) {
                    SoundPlayer.playSound("alert.wav"); // 경고음 재생
                    JOptionPane.showMessageDialog(null, "큐가 가득 찼습니다!", "오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String value = JOptionPane.showInputDialog("삽입할 값을 입력하세요:"); // 값 입력 받기
                if (value != null && !value.trim().isEmpty()) {
                    rear = (rear + 1) % MAX_SIZE; // rear 인덱스 갱신
                    queue[rear] = value; // 값 삽입

                    canvas.repaint(); // 캔버스 다시 그리기
                    showImage("enqueue.jpg"); // 삽입 이미지를 표시
                    blinkQueue(); // 큐 깜빡임 효과 실행
                }
            });

            btnDelete.addActionListener(e -> {
                if (front == rear) {
                    SoundPlayer.playSound("alert.wav"); // 큐가 비어 있을 때 경고음 재생
                    JOptionPane.showMessageDialog(null, "큐가 비어 있습니다!", "오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                front = (front + 1) % MAX_SIZE; // 마지막 원소 삭제 후 큐 비우기
                queue[front] = null; // 첫 번째 원소 삭제
                canvas.repaint(); // 캔버스 다시 그리기
                showImage("dequeue.jpg"); // 삭제 이미지를 표시
            });

            setVisible(true); // 창 표시
        }

        // 큐 상태를 파일에 저장하는 메서드
        private void saveQueueToFile(String fileName) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                for (int i = 0; i < MAX_SIZE; i++) {
                    if (queue[i] != null) {
                        writer.write(queue[i] + "\n"); // 각 데이터를 파일에 저장
                    } else {
                        writer.write("null\n"); // 비어있는 부분을 null로 저장
                    }
                }
                writer.write(front + "\n"); // front 값 저장
                writer.write(rear + "\n");  // rear 값 저장
                JOptionPane.showMessageDialog(this, "큐 상태가 저장되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "파일 저장 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // 파일에서 큐 상태를 불러오는 메서드
        private void loadQueueFromFile(String fileName) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                for (int i = 0; i < MAX_SIZE; i++) {
                    String line = reader.readLine();
                    if ("null".equals(line)) {
                        queue[i] = null; // 비어있는 부분 복원
                    } else {
                        queue[i] = line; // 큐 데이터 복원
                    }
                }
                front = Integer.parseInt(reader.readLine()); // front 값 불러오기
                rear = Integer.parseInt(reader.readLine());  // rear 값 불러오기

                canvas.repaint(); // 큐 시각화 갱신
                JOptionPane.showMessageDialog(this, "큐 상태가 불러와졌습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "파일 불러오기 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // 삽입/삭제 이미지를 일시적으로 표시하는 메서드
        private void showImage(String imagePath) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource(imagePath)); // 이미지 로드
                imageLabel.setIcon(icon); // 레이블에 이미지 설정
                imageLabel.setVisible(true); // 레이블 표시

                // 기존 타이머가 실행 중이면 중지
                if (imageTimer != null && imageTimer.isRunning()) {
                    imageTimer.stop();
                }

                // 새 타이머 시작
                imageTimer = new Timer(1000, e -> imageLabel.setVisible(false)); // 1초 후 레이블 숨기기
                imageTimer.setRepeats(false); // 타이머가 한 번만 실행되도록 설정
                imageTimer.start(); // 타이머 시작
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "이미지를 로드할 수 없습니다: " + imagePath, "오류", JOptionPane.ERROR_MESSAGE);
            }
        }

        // 큐의 특정 요소를 깜빡이도록 애니메이션 처리하는 메서드
        private void blinkQueue() {
            // 이미 깜빡임이 진행 중이거나 깜빡일 대상이 없으면 메서드 종료
            if (isBlinking || rear == -1) return;
            isBlinking = true;

            // 깜빡임 애니메이션 처리를 위한 스레드 시작
            Thread blinkThread = new Thread(() -> {
                try {
                    for (int i = 0; i < 6; i++) { // 6번 깜빡임 (3번 반복)
                        blinkCounter = i % 2; // 0(꺼짐)과 1(켜짐) 상태를 번갈아 변경
                        canvas.repaint(); // 캔버스를 다시 그려 상태 반영
                        Thread.sleep(300); // 깜빡임 간격 300ms
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace(); // 디버깅용 예외 출력
                } finally {
                    // 깜빡임 상태 초기화 및 캔버스를 다시 그려 원래 상태로 복원
                    isBlinking = false;
                    blinkCounter = 0; // 깜빡임 카운터 초기화
                    canvas.repaint(); // 최종 상태를 화면에 반영
                }
            });
            blinkThread.start(); // 스레드 실행
        }

        // 원형 큐를 그래픽으로 표시하는 사용자 정의 JPanel 클래스
        class CircleCanvas extends JPanel {

            public CircleCanvas() {
                setBackground(Color.WHITE); // 캔버스 배경을 흰색으로 설정
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // 부모 클래스의 paint 메서드 호출
                Graphics2D g2 = (Graphics2D) g; // 업캐스팅을 해야 안티앨리어싱 제공
                // 그래픽 품질 향상을 위한 안티앨리어싱 설정
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 원형 큐를 그리기 위한 중심 좌표와 반지름 계산
                int width = getWidth(); // 패널의 가로
                int height = getHeight();

                int centerX = width / 2; // 패널의 중심 좌표
                int centerY = height / 2;
                int radius = Math.min(width, height) / 3; // 패널 크기에 따라 반지름 동적으로 조정한다

                Font font = new Font("SansSerif", Font.PLAIN, 14); // 기본 폰트 설정
                g2.setFont(font);
                FontMetrics fm = g2.getFontMetrics(); // 텍스트 너비 높이 계산 (텍스트 가운데 정렬하기위해)

                // 큐의 각 요소를 원형 배치로 그림
                for (int i = 0; i < MAX_SIZE; i++) {
                    // 요소의 인덱스에 따라 위치 계산
                    double angle = Math.toRadians((360.0 / MAX_SIZE) * i - 90); // 12시 방향을 시작점
                    int x = (int) (centerX + radius * Math.cos(angle));
                    int y = (int) (centerY + radius * Math.sin(angle));

                    // 새로 삽입된 데이터만 깜빡임 처리 그리고 색 지정
                    if (i == rear && front != rear) {
                        if (blinkCounter == 1) {
                            g2.setColor(Color.YELLOW); // 깜빡이는 색상
                        } else {
                            g2.setColor(Color.WHITE); // 통일된 기본 색상
                        }
                    } else if (queue[i] != null) {
                        g2.setColor(Color.WHITE); // 통일된 기본 색상
                    } else {
                        g2.setColor(Color.GRAY); // 비어 있는 경우
                    }

                    // 원형 슬롯 그리기
                    g2.fillOval(x - 35, y - 30, 60, 60); // 슬롯 내부 색상 칠하기
                    g2.setColor(Color.BLACK);
                    g2.drawOval(x - 35, y - 30, 60, 60); // 슬롯 외곽선 그리기

                    // 텍스트 출력 (가운데 정렬 및 길이 조정)
                    if (queue[i] != null) {
                        String text = queue[i];
                        if (fm.stringWidth(text) > 50) { // 텍스트 길이가 원을 넘으면 자르기
                            text = text.substring(0, Math.min(text.length(), 5)) + "...";
                        }
                        int textWidth = fm.stringWidth(text);
                        int textHeight = fm.getAscent();
                        g2.drawString(text, x - textWidth / 2 - 4, y + textHeight / 4 + 2);
                    }

                    // 인덱스 위치에 "FRONT" 또는 "REAR" 라벨 추가
                    if (i == front) {
                        g2.setColor(Color.BLUE);
                        g2.drawString("FRONT: " + i, x - 30, y - 40);
                    }
                    if (i == rear && queue[i] != null) {
                        g2.setColor(Color.RED);
                        g2.drawString("REAR: " + i, x - 30, y + 55);
                    }
                }
            }
        }
    }

    // 사운드 파일을 재생하기 위한 유틸리티 클래스
    public static class SoundPlayer {
        public static void playSound(String fileName) {
            try {
                InputStream resourceStream = SoundPlayer.class.getResourceAsStream("/teamproject/" + fileName);
                if (resourceStream == null) {
                    throw new FileNotFoundException("리소스 파일을 찾을 수 없습니다: /teamproject/" + fileName);
                }

                // BufferedInputStream으로 감싸기
                BufferedInputStream bufferedIn = new BufferedInputStream(resourceStream);

                // 오디오 스트림 생성
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);

                // 오디오 재생
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.err.println("오디오 파일 재생 오류: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new DataStructureGUI();
    }
}