# RestApiReturnToImage

## 이슈사항
* 우리서비스에서는 map-static을 이용해서 지점위치를 간략히 보여주고 그림 클릭 시 상세 지도로 넘어간다
* 네이버 maps의 map-static api가 변경된다.
* 기존에는 주소값에 key들을 넣어서 보냈는데 header에 넣는 방식으롤 변경된다.
* 그런데 웹에서 호출을 해보니 CORS에러가 난다.(<https://www.ncloud.com/support/faq/all/3512/>)
* 위의 사유로 인하여 서버측으로 호출하였을때 base64로 encoding하여 내려준값으로 그림을 그리는 방법으로 진행(왜 BASE64? : <https://effectivesquid.tistory.com/entry/Base64-%EC%9D%B8%EC%BD%94%EB%94%A9%EC%9D%B4%EB%9E%80/>)