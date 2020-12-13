// Go 버튼에 이벤트 등록
const gotoButton = document.querySelector('#gotoButton');

gotoButton.addEventListener('click', function () {
  // form 에 담긴 longUrl
  const shortUrl = document.getElementById('inputShortUrl').value;

  // longUrl 미입력 또는 잘못된 형식 입력 시 알림
  if (shortUrl === '') {
    alert('shortUrl 을 입력하세요.');
    return;
  } else if (shortUrl.indexOf('smile.psh/') !== 0) {
    alert('잘못된 형식의 shortUrl 입니다.');
    return;
  }

  const data = {};
  data.short_url = shortUrl.substr(10, shortUrl.length);
  console.log(data);

  $.ajax({
    type: 'POST',
    url: 'http://localhost:8080/api/url/original',
    contentType: 'application/json',
    data: JSON.stringify(data),
    dataType: 'text',
    success: function (data, response) {
      console.log(data);

      // input 값 비워주기
      $('#inputShortUrl').val('');

      // 새 페이지 열기
      window.open(data, '_blank');

      // 페이지 redirect 하기
      window.location.replace('/');
    },
    error: function (request, status, error, data) {
      alert(request.responseText);
      // console.log("code = "+ request.status + " message = " + request.responseText + " error = " + error);
    }
  });
});
