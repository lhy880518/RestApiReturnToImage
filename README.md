# RestApiReturnToImage

## 이슈사항
* 우리서비스에서는 map-static을 이용해서 지점위치를 간략히 보여주고 그림 클릭 시 상세 지도로 넘어간다
* 네이버 maps의 map-static api가 변경된다.
* 기존에는 주소값에 key들을 넣어서 보냈는데 header에 넣는 방식으롤 변경된다.
* 그런데 웹에서 호출을 해보니 CORS에러가 난다.(<https://www.ncloud.com/support/faq/all/3512/>)
* 위의 사유로 인하여 서버측으로 호출하였을때 base64로 encoding하여 내려준값으로 그림을 그리는 방법으로 진행(왜 BASE64? : <https://effectivesquid.tistory.com/entry/Base64-%EC%9D%B8%EC%BD%94%EB%94%A9%EC%9D%B4%EB%9E%80/>)

## 2019.04.09
* 이런생각이 시작이었다. 위의 encoding이미지값을 서버 혹은 클라이언트의 특정 저장소에 저장해놓고 만약 값을 가지고 있음이 판단된다면 네이버 api를 호출하지 말자 이런생각을 한 이유는 아래와 같다
![네이버 api변경사항](/src/main/resources/static/image/naverApiPay.png)
    올해까지만 무료래요
* 클라이언트, 서버 저장공간? 아 세션, 쿠키, 캐시 - <https://gist.github.com/lhy880518/1d5e9c00df0c5c12db67756af85c885c/>
    * 살펴보다보니 간과한 점이 보였다. 모든 저장공간 이라는것은 거기에 저장하면 자원을 사용하는 것이었다.
    * base64로 인코딩된 값을 세션,쿠키,캐시등에 저장하려는것은 각 그릇에 넘처흐를정도의 데이터를 담는 행위라는것을
    * 또한 옆에서 지켜보시던 한 개발자분께서 해당 api의 호출을 줄여서 요금정책에 이득을 가져 가는 부분은 
    단말 또한 개선해야되는 부분이라고 하셔서(단말에서는 CORS에러가 나질않아서 생각을 못함) 일단은 호출관련 요금문제는 추후 개선하자고 하셨다.
* 결론적으로 base4인코딩한 데이터를 아래와 같이 뿌려주는 방식으로 채택하였다.
```javascript
<img id="imgObj" src="">
<script type="text/javascript">
    function getImageEncodingBase64(){
        $.ajax({
            url : "/getImageEncodingBase64",
            method : "GET",
            success : function(data){
                console.log(data);
                $("#imgObj").attr("src","data:image/jpeg;base64,"+data);
            },
            error : function(data){
                console.log(data);
            }
        });
    }
</script>
```

## 2019.04.10
* 아침에 출근하면서 뭔가 착각했다는걸 깨달았다.
* 굳이 base64로 인코딩해서 바이트 코드로 내려줄 필요가 없다는걸
* 아래와 같이 처리하면 이미지가 바로 노출된다. 
```javascript
<img src="/getImageWithMediaType/">
```
```java
@GetMapping(value = "/getImageWithMediaType",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    @ResponseBody
    public byte[] getImageWithMediaType() throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.add("X-NCP-APIGW-API-KEY", clientSecret);

        ResponseEntity<byte[]> response = new RestTemplate().exchange(apiURL, HttpMethod.GET, new HttpEntity(headers), byte[].class);

        return response.getBody();
    }
```
