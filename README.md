# 겨울방학 자바 프로젝트

## XML로 구동되는 Java Swing GUI 기반 게임 에뮬레이터 +
## XML을 만드는 에디터

### 개발기간 : 2024 01월 ~ 2024 02월 (약 2개월) => xml Parser 1주일, 에뮬레이터 2주일, Editor 4주일, Android 공부 1주일
### 사용 언어 : Java, XML, kotlin(Android)
### 사용 라이브러리 : Java Swing(GUI)


## 에뮬레이터
### 프로그램 구조
![서준영_보고서_2024_2월_xml보고서](https://github.com/Jun-Young-Seo/JAVA_SwingGUI_XML_Game_Emulator_And_Editor/assets/128452954/219b89ef-49e7-4fd4-94a9-344fe92aee94)

### 사용법 및 설명
XMLReader 클래스는 xmlFile의 파싱 결과를 바탕으로 필요한 Images, Sounds 파일을 로딩해 BlockGameFrame에 전달한다. BlockGameFrame은 xmlFile의 파싱 결과를 바탕으로 게임 오브젝트인 Block, 플레이어 
오브젝트인 BulletBlock을 동적으로 생성해 게임을 실행하게 된다. BlockGameMenuBar 클래스는 xmlFile을 사용자가 선택할 수 있도록 하는 인터페이스를 제공한다.

사용자는 좌측 상단의 파일 버튼을 눌러 원하는 xml 파일을 실행시킬 수 있다.


## 게임용 XML 에디터
사용자가 직관적으로 xml 파일을 제작할 수 있도록 드래그, 클릭, 마우스 휠 등을 활용해 오브젝트를 배치할 수 있도록 제작했으며, 사용자가 설정한 속성은 곧 xml 파일의 태그로 자동으로 변환된다.
 xml 플랫폼을 구성하는데 있어 에디터의 역할을 담당한다. 이 프로그램과 JVM, 에뮬레이터가 적재되어 있다면, 어느 환경에서나 실행할 수 있는 게임 xml 파일을 제작할 수 있다. Java GUI 프레임워크인 Java Swing을 활용해 화면을 구성했기 때문에 JVM 환경이 필수적이다.
 에디터의 코드를 다양한 xml파서와 xml 에디터를 제작하는데 활용할 수 있다. 에디터와 에뮬레이터, xml 파일을 활용해 궁극적으로 제작하고자 한 플랫폼을 만들 수 있다. xml 에디터를 활용하는 사용자, 즉 제작자 역시 코드를 작성할 줄 몰라도 프로그램을 제작할 수 있도록 만들어졌기 때문이다.


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
배경화면 설정을 마치면 자동으로 ArrangePanel에 배경화면 이미지가 적용된다. 또, 우측 팔레트의 각 블록을 배치했다. ArrangePanel에서 선택한 오브젝트는 선택된 개체를 알 수 있도록 빨간색 테두리로 표시된다. 오브젝트는 마우스 휠을 통해 크기를 확대 또는 축소할 수 있으며 마우스 드래그를 통해 이동할 
수도 있다. 사진은 위에서 설정한 enemy1.png 이미지를 가진 오브젝트를 선택하고 마우스 휠을 통해 확대한 모습이다.

![7](https://github.com/Jun-Young-Seo/JAVA_SwingGUI_XML_Game_Emulator_And_Editor/assets/128452954/cb877bc2-04d1-4d99-9209-ed3a13633897)
오브젝트를 클릭한 채로 ItemPanel의 속성 탭을 클릭하면 각 개체의 속성 정보를 확인할 수 있다. 또, 위와 동일한 방식으로 플레이어 개체도 추가한 모습이다.

![8](https://github.com/Jun-Young-Seo/JAVA_SwingGUI_XML_Game_Emulator_And_Editor/assets/128452954/bba30ba1-afad-4e20-a6d0-028724924015)
ArrangePanel의 XML 탭을 클릭한 화면이다. 현재 작업 중인 Editor 화면을 저장하면 어떤 xml 파일로 생성되는지 직접 확인할 수 있다. 

![9](https://github.com/Jun-Young-Seo/JAVA_SwingGUI_XML_Game_Emulator_And_Editor/assets/128452954/7f88575e-4933-4bfd-b6de-e0d39313f2e4)
프레임 상단의 File 버튼을 눌러 기존에 작업 중이던 다른 xml 파일을 연 모습이다. 사용자가 팔레트에 저장해 둔 개체는 변경되지 않는다. 이 Editor 프로그램 하나로 여러 xml 파일을 동시에 수정할 수 있다.


프로그램의 Main Class가 되는 MainAuthorFrame에는 settings와 arrangeBlocks 이름의 두 벡터가 있다. settings 벡터는 게임의 설정이 저장될 용도의 벡터로, ItemPanel과 ArrangePanel에서 게임 설정 값을 보관한다. arrangeBlocks는 게임 오브젝트의 설정이 저장될 용도의 벡터로, 각 오브젝트들의 설정 값과 개수, 이미지 등이 저장된다.
 MainAuthorFrame의 함수 makeString()이 호출되면 두 벡터의 모든 값들이 xml 코드로 변환된다. 이 함수는 사용자가 Editor의 Save 또는 Save as 버튼을 누르거나, ArrangePanel의 xml 탭을 클릭하면 호출되어 모든 값을 xml 코드로 변환한다.

![image](https://github.com/Jun-Young-Seo/JAVA_SwingGUI_XML_Game_Emulator_And_Editor/assets/128452954/1f51a2b0-e628-44db-8051-df6101c7d9b4)
이 에디터로 제작한 간단한 xml 파일을 안드로이드 기반 환경에서 작동한 모습



## 프로젝트를 통해 느낀 점

처음부터 끝까지 완성해 본 프로젝트 중 가장 큰 프로젝트였다. 처음에 교수님께서 xml 기반 에뮬레이터, 에디터 프로그래밍을 제안해 주셨다. 시작할 때는 별 생각 없이(아이디어는 간단했으니까) 시작해서 주먹구구식으로 코드를 짰다.

첫 번째 문제가 여기서 발생했다. 코드가 너무 많아져서, 길어져서 나중엔 감당이 안됐던 것이다. 클래스 설계를 대충 머릿속으로만 해서 일단 코딩을 시작했기 때문이었다.
결국 모든 코드를 지워버리고 클래스부터 다시 설계했다. 완벽하진 않지만 Emulator랑 Editor 모두 클래스부터 설계해서 구조를 짰다. 확실히 구조를 잡아 두고 코딩하는 것과 그렇지 않은 것에는 큰 차이가 있음을 배울 수 있었다.
다음 학기에 배우게 될 Software Engineering 과목에서 이런 설계 패턴에 대해 공부한다고 한다. 이 과목을 수강할 때 남들보다는 조금 더 깊이 있게 들을 수 있는 경험을 한 것 같다.

두 번째 문제는 개발 시간의 분배와 집중도였다. 2학년 2학기 겨울 방학 프로젝트를 시작하고 부터 정말 많은 시간을 프로그래밍에 쏟아 부었다. 이렇게까지 몰두해서 재밌게 한 적도 처음이었던 것 같지만, 결과물은 들인 시간 대비 썩 맘에 들진 않았다. 
프로그래밍을 하며 막힐 때 마다 해결할 방법에 대해 고민을 하며 보내는 시간이 너무 많았기 때문이다. 또, 첫 번째 문제와 같이 클래스 구조를 잡아 두고 프로젝트를 시작했더라면 훨씬 효율적인 시간을 보낼 수 있었을 듯 하다.
막혔던 문제 중 가장 어려웠던 것은 Null Pointer Exception이었다. Player는 사실 그렇게 어렵지 않게 완성했었는데,
Editor의 SettingPanel과 ItemPanel, ArrangePanel이 상호 참조하면서 모두 NULL이 되는 문제가 발생했었다. 이 문제를 해결하려면 init()을 활용하거나, 코드 전체를 뒤집어 엎는 수준의 수정이 필요했다.(내가 원하는 기능은 꼭 만들고싶어서...)
결국 init()을 이리저리 잘 써서 해결하긴 했는데, 뭔가 코드를 누더기처럼 기운 느낌이라 썩 마음에 들지 않았다. 역시 첫 번째 문제와 같이 기획을 깔끔하게 해냈더라면 이런 문제도 없지 않았을까.

앞으로도 이런 프로젝트를 할 기회가 또 있다면, 그 때는 먼저 프로젝트 주제를 바탕으로 클래스 구조를 짜고, 프로그램 전체를 어떤 식으로 제작할지에 대한 생각을 하는 데에 훨씬 많은 시간을 분배해야겠다. 이번 프로젝트를 통해
IT 기업에서 뛰어난 경력의 실력자가 결국 기획자가 된다는 것이 무슨 말인지 마음 깊이 이해할 수 있었다. 만약 내가 좋은 기획을 했었더라면 훨씬 편하고, 빠르게, 완성도도 더 높게 이 프로젝트를 완성했을 것이다.


그래도 잘 했다고 느껴지는 점 하나는 포기하지 않고 끝까지 붙잡고 완성했다는 점이다. 만약 혼자 했다면(결국 모든 코딩은 혼자 한거긴 하지만) 중간에 포기했을 것이다. 프로젝트를 하며 매주 교수님과 다른 학우들과
온라인으로 화상 미팅을 하며 각자가 한 프로그램과 코드를 가볍게 공유했었는데, 이 활동이 나에게 큰 자극이었다. 아이디어가 안나오고 문제에서 막혀도 마감 기한같은 느낌이라 어떻게든 붙잡고 있게 되기도 했고, '다른 사람은 이정도 하는구나'이런 아이디어도 있네?', '이렇게 해결하기도 하는구나' 하며 새로운 시각을 많이 얻어갈 수 있었다.


프로젝트를 하나씩 해나갈 때 마다 조금씩이나마 실력이 발전하는 것 같다! 앞으로도 매 학기 중, 또 방중마다 다양한 프로젝트에 참여해 좋은 경험을 쌓고, 또 실력을 키워나갈 수 있었으면 좋겠다 :)
