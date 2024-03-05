# 겨울방학 자바 프로젝트

## XML로 구동되는 JAVA SWING GUI 기반 게임 +
## 게임용 XML 에디터

사용자가 직관적으로 xml 파일을 제작할 수 있도록 드래그, 클릭, 마우스 휠 등을 활용해 오브젝트를 배치할 수 있도록 제작했으며, 사용자가 설정한 속성은 곧 xml 파일의 태그로 자동으로 변환된다.
 xml 플랫폼을 구성하는데 있어 에디터의 역할을 담당한다. 이 프로그램과 JVM, 에뮬레이터가 적재되어 있다면, 어느 환경에서나 실행할 수 있는 게임 xml 파일을 제작할 수 있다. Java GUI 프레임워크인 Java Swing을 활용해 화면을 구성했기 때문에 JVM 환경이 필수적이다.
 에디터의 코드를 다양한 xml파서와 xml 에디터를 제작하는데 활용할 수 있다. 에디터와 에뮬레이터, xml 파일을 활용해 필자가 궁극적으로 제작하고자 한 플랫폼을 만들 수 있다. xml 에디터를 활용하는 사용자, 즉 제작자 역시 코드를 작성할 줄 몰라도 프로그램을 제작할 수 있도록 만들어졌기 때문이다.

## 게임 제작용 XML 에디터 사용 방법
### 프로그램 구조
![image](https://github.com/Jun-Young-Seo/JAVA_SwingGUI_XML_Game_Emulator_And_Editor/assets/128452954/56c6794f-7a4e-4ccd-b1e6-359c794ae446)
![image](https://github.com/Jun-Young-Seo/JAVA_SwingGUI_XML_Game_Emulator_And_Editor/assets/128452954/dc0a5fc4-cc31-423e-8b11-88851d7635aa)


## 사용법 및 설명
Editor 프로그램의 클래스 구조를 도식화한 그림이다. Editor를 활용해 기존에 작업 중이던 xml 파일 또는 새로운 xml 파일에 사용자 리소스(Images, Sounds)를 추가해 xml 파일을 제작할 수 있다. ItemPanel에 오브젝트들을 미리 저장해 편하게 활용할 수 있다. 또 ItemPanel에서는 각종 오브젝트의 속성을 지정하고, 그 속성을 수치로 입력할 수도 있다. 외에도 게임의 규칙이나 스코어보드의 폰트 등을 설정할 수 있다.
ArrangePanel에서는 실제 게임 화면처럼 오브젝트들을 배치해볼 수 있다. 게임의 프로토타입을 직접 눈으로 살펴보고, 마우스 드래그, 휠 등을 활용해 직관적으로 오브젝트를 배치할 수 있도록 제작됐다. 또, 현재 화면 배치상의 오브젝트들이 XML 파일로 어떻게 표현되는지도 확인할 수 있도록 했다.
![1](https://github.com/Jun-Young-Seo/JAVA_SwingGUI_XML_Game_Emulator_And_Editor/assets/128452954/977e4c3f-deb9-4e2e-a79b-4d98f30069a5)
Editor 프로그램의 초기 화면이다. 화면 분할 기준 우측 패널이 ItemPanel, 좌측 패널이 ArrangePanel이다. ItemPanel의 블록 이미지들은 게임 오브젝트로, 비행기 이미지들은 플레이어의 역할이 된다.
![2](https://github.com/Jun-Young-Seo/JAVA_SwingGUI_XML_Game_Emulator_And_Editor/assets/128452954/6b83da0d-ceee-412a-b3a5-bfeba7d3355c)
게임 오브젝트(블록 모양)를 더블클릭해 각 이미지와 속성을 추가할 수 있다. 저장 버튼을 눌러 속성을 저장하고, 블록 배치하기를 통해 블록을 ArrangePanel에 배치할 수 있다.
![3](https://github.com/Jun-Young-Seo/JAVA_SwingGUI_XML_Game_Emulator_And_Editor/assets/128452954/0c14fef3-942a-4825-833a-d49641845130)
ItemPanel은 사용자가 팔레트처럼 활용할 수 있도록 제작됐다. 저장 버튼을 눌러 속성을 저장해 둔 오브젝트들은 언제든 화면에 배치할 수 있다. 더블 클릭을 통해 속성을 수정할 수도 있고, 배치하기 버튼을 눌러 바로 배치할 수도 있다. 또, 마우스 오른쪽 클릭으로 오브젝트를 클릭하면 화면에 지정해둔 속성(없을 경우 디폴트 값)으로 배치된다.
![4](https://github.com/Jun-Young-Seo/JAVA_SwingGUI_XML_Game_Emulator_And_Editor/assets/128452954/664ac6d2-5288-4240-820b-c973ee802d70)
ItemPanel 위의 설정 탭으로 이동해 설정 버튼을 누른 화면이다. 게임의 각종 규칙과 배경화면, 사운드, 게임 프레임 크기 등을 설정할 수 있다. 중앙의 점수판 설정 버튼을 클릭하면 스코어보드 설정 창을 확인할 수 있다.
![5](https://github.com/Jun-Young-Seo/JAVA_SwingGUI_XML_Game_Emulator_And_Editor/assets/128452954/0f5c0b20-037a-4eac-8b1a-6c3d44e94a40)
점수판 설정 버튼을 클릭하면 스코어보드 설정 창 화면을 확인할 수 있다.
![6](https://github.com/Jun-Young-Seo/JAVA_SwingGUI_XML_Game_Emulator_And_Editor/assets/128452954/325b1787-5bcd-4581-8731-2341f252cfe3)
배경화면 설정을 마치면 자동으로 ArrangePanel에 배경화면 이미지가 적용된다. 또, 우측 팔레트의 각 블록을 배치했다. ArrangePanel에서 선택한 오브젝트는 선택된 개체를 알 수 있도록 빨간색 테두리로 표시된다. 오브젝트는 마우스 휠을 통해 크기를 확대 또는 축소할 수 있으며 마우스 드래그를 통해 이동할 수도 있다. 사진은 위에서 설정한 enemy1.png 이미지를 가진 오브젝트를 선택하고 마우스 휠을 통해 확대한 모습이다.
![7](https://github.com/Jun-Young-Seo/JAVA_SwingGUI_XML_Game_Emulator_And_Editor/assets/128452954/cb877bc2-04d1-4d99-9209-ed3a13633897)
오브젝트를 클릭한 채로 ItemPanel의 속성 탭을 클릭하면 각 개체의 속성 정보를 확인할 수 있다. 또, 위와 동일한 방식으로 플레이어 개체도 추가한 모습이다.

![8](https://github.com/Jun-Young-Seo/JAVA_SwingGUI_XML_Game_Emulator_And_Editor/assets/128452954/bba30ba1-afad-4e20-a6d0-028724924015)
ArrangePanel의 XML 탭을 클릭한 화면이다. 현재 작업 중인 Editor 화면을 저장하면 어떤 xml 파일로 생성되는지 직접 확인할 수 있다. 

![9](https://github.com/Jun-Young-Seo/JAVA_SwingGUI_XML_Game_Emulator_And_Editor/assets/128452954/7f88575e-4933-4bfd-b6de-e0d39313f2e4)
프레임 상단의 File 버튼을 눌러 기존에 작업 중이던 다른 xml 파일을 연 모습이다. 사용자가 팔레트에 저장해 둔 개체는 변경되지 않는다. 이 Editor 프로그램 하나로 여러 xml 파일을 동시에 수정할 수 있다.

### 프로젝트를 통해 느낀 점

