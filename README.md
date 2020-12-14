# url-shortener
Smilegate DEV Camp - PJ1. Url Shortener

## 기능
1. long Url -> short Url 변환

2. short Url을 통해 original Url로 연결해주기

3. 분석 결과 제공
- Total Count
- Top5 자주 클릭된 Url
- 클릭 수 Pie Chart

## 스크린샷
<img width="350" alt="스크린샷 2020-12-14 오전 12 51 31" src="https://user-images.githubusercontent.com/26567880/102032943-7b532980-3dfd-11eb-91fc-1036e878a2c3.png">
<img width="700" alt="스크린샷 2020-12-14 오전 12 51 47" src="https://user-images.githubusercontent.com/26567880/102032949-7ee6b080-3dfd-11eb-8416-33647b3d025d.png">
<img width="700" alt="스크린샷 2020-12-14 오전 12 51 59" src="https://user-images.githubusercontent.com/26567880/102032951-81e1a100-3dfd-11eb-9caf-bbbad3c0dc23.png">

## 스택
- Spring Boot
- MySQL
- Redis
- HTML/CSS
- Javascript

## API
|Method|Path|Explanation|
|------|----|-----------|
|POST  |/shortening|long Url을 DB에 저장 후, id 값을 Base62로 인코딩하여 short Url으로 변환 후, 최신 정보 담긴 UrlInfo 받아옴|
|POST  |/original  |short Url에서 original Url 받아옴. 없는 short Url일 경우, NOT_FOUND Status 리턴|
|GET   |/analysis  |total Count(변환된 주소 개수), 상위 5위 Click Count의 UrlInfo 리스트, Pie chart 그리기 위한 전체 UrlInfo 리스트 받아옴|

## 아키텍쳐
<img width="984" alt="스크린샷 2020-12-14 오후 12 11 02" src="https://user-images.githubusercontent.com/26567880/102036218-99bd2300-3e05-11eb-8934-93ddb79276f1.png">


## ServiceImpl
- 구체적인 로직은 코드에 주석으로 표현


