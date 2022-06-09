# GithubHelper
## 프로젝트 설명
- Github Helper는 본인의 깃허브 정보와 친구들의 깃허브 정보를 한 눈에 볼 수 있는 어플리케이션입니다.

- Github Helper는 깃허브 정보를 친구들과 공유하는 문화를 가지기 위해 제작하였으며, 해당 어플리케이션을 통해 이전보다 쉽게 유저의 깃허브 정보와 친구들의 깃허브 정보를 얻을 수 있습니다.

## Structure

![image](https://user-images.githubusercontent.com/20418155/172809227-ba98d2af-d2dc-4382-98cf-02e40b787e01.png)

## Application

![image](https://user-images.githubusercontent.com/20418155/172809305-1bda67c4-b43a-4354-9133-ef23c13a0b08.png)

MVVM 패턴을 사용해 제작하였습니다.

  ### Model
  - 총 5개의 모델이 있습니다.
  
    #### BaseModel
    - 통신을 위한 기본 모델입니다. 성공여부의 정보와 메시지 정보를 담고 있습니다.
    
    #### UserModel
    - 사용자의 정보에 관한 모델입니다. 통신 성공 여부 확인을 위해 BaseModel을 상속합니다.
    
    #### GithubInfo
    - 깃허브 정보에 관한 모델입니다.
    
    #### Project
    - 각 프로젝트의 정보에 관한 모델입니다. GithubInfo 내에 List 형태로 저장되어 있습니다.
    
    #### AddFriendModel
    - 친구 추가 통신을 위한 모델입니다. Request를 보내기 위함 이므로, BaseModel을 상속하지 않습니다.
    

  ### View
  - 총 5개의 view(LoginActivity, SignUpActivity, MainActivity, DetailActivity, popup)가 있습니다.

  ### View Model
  - 총 4개의 ViewModel과 2개의 Adapter가 있습니다. 

    #### LoginViewModel
    - Login Activity 내에 있는 ui에 기능을 추가하기 위한 view model입니다.
    #### SignUpViewModel
    - SignUp Activity 내에 있는 ui에 기능을 추가하기 위한 view model입니다.
    #### MainViewModel (+ Github Adapter, Project Adapter)
    - Main Activity 내에 있는 ui와 UserModel, GithubInfo, Project 모델과 연동하고 각 adapter를 활용해 ui를 세팅하고, ui에 기능을 추가하기 위한 view model입니다. Popup 또한 해당 view model에서 관리합니다.
    #### DetailViewModel
    - Detail Activity 내에 있는 ui와 Project 모델과 연동해 ui를 세팅하고, ui에 기능을 추가하기 위한 view model입니다.


## Server

![image](https://user-images.githubusercontent.com/20418155/172809381-a017e996-f047-4bf8-90ff-821e5ae62ea1.png)

  서버의 api는 HTTP/1.1 프로토콜을 사용해 RESTful 하게 제작하였습니다.

  ### /user (method=GET)
  - 사용자의 정보를 반환하는 api입니다. DB에 등록된 사용자의 정보뿐만 아니라 사용자의 깃허브 정보 또한 얻을 수 있습니다.

  ### /user (method=POST)
  - 사용자를 등록하는 api입니다. 회원가입 시 사용하며, 해당 api를 통해 유저의 정보가 DB에 저장되고, 성공 여부를 반환합니다.

  ### /login (method=POST)
  - 로그인 api입니다. 유저의 id를 DB에서 확인해 로그인 성공 여부를 반환합니다.

  ### /friends (method=GET)
  - 유저의 친구들의 정보를 반환하는 api입니다. 친구들의 깃허브 정보와 프로젝트 정보를 반환합니다.

  ### /friend (method=POST)
  - 유저의 친구를 등록하는 api입니다. 유저 테이블의 friends 정보를 수정하며, 성공여부를 반환합니다.


## 구현 형태

![image](https://user-images.githubusercontent.com/20418155/172809451-4586b56c-aadf-4591-a3e9-54fabcfd59b8.png)


  주요 액티비티는 총 4가지입니다.
  ### Login Activity
  - 로그인을 위한 액티비티입니다. “로그인” 버튼과 “회원가입” 버튼이 있으며, 로그인이 정상적으로 이루어질 경우 Main Activity로 이동하며, 회원가입 버튼을 누르면 Signup Activity로 이동합니다.
  ### SignUp Activity
  - 회원가입을 위한 액티비티입니다. 회원가입이 정상적으로 이루어지면 해당 액티비티를 종료하고, 그로 인해 이전 액티비티였던 Login Activity로 이동합니다.
  ### Main Activity
  - 사용자의 깃허브 정보와 친구등록을 한 친구들의 깃허브 정보를 볼 수 있는 액티비티입니다. 사용자(친구)의 이름을 클릭하면 사용자(친구)의 깃허브 페이지(웹사이트)로 이동합니다. 프로젝트 text view를 클릭하면 해당 프로젝트에 대한 정보가 담긴 detail activity로 이동합니다. “+” 플로팅 버튼을 클릭하면 친구를 등록할 수 있는 팝업창을 열 수 있습니다.
  ### Detail Activity
  - 클릭한 프로젝트의 상세 정보를 볼 수 있는 액티비티입니다. “GO TO PROJECT”라는 버튼이 있으며, 해당 버튼을 클릭하면 해당 프로젝트의 깃허브 페이지(웹사이트)로 이동합니다.

  추가 액티비티는 팝업창이 있습니다.

  ### Popup
  - 친구추가를 할 수 있는 팝업창(Dialog)입니다. 추가 버튼을 누르면 친구추가를 할 수 있으며, 팝업창 밖을 클릭하면 팝업창은 사라집니다.

