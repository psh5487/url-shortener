// 줄이기 제출 버튼에 이벤트 등록
const shorteningButton = document.querySelector('#submitButton');

shorteningButton.addEventListener('click', function () {
  // form 에 담긴 longUrl
  const longUrl = document.getElementById('inputUrl').value;

  // longUrl 미입력 시 알림
  if (longUrl === '') {
    alert('longUrl 을 입력하세요.');
    return;
  }

  const data = {};
  data.long_url = longUrl;
  console.log(data);

  $.ajax({
    type: 'POST',
    url: 'http://localhost:8080/api/url/shortening',
    contentType: 'application/json',
    data: JSON.stringify(data),
    dataType: 'json',
    success: function (data, response) {
      console.log(data);
      appendShortUrl(data);
    },
    error: function (request, status, error, data) {
      console.log('code = ' + request.status + ' message = ' + request.responseText + ' error = ' + error);
    }
  });
});

function appendShortUrl (resData) {
  // input 값 비워주기
  $('#inputUrl').val('');

  // a tag 만들기
  const a = document.createElement('a');
  a.setAttribute('href', resData.long_url);
  a.setAttribute('target', '_blank');
  a.setAttribute('id', 'aTag');

  const fullShortUrl = 'smile.psh/' + resData.short_url;
  a.innerHTML = fullShortUrl;

  document.querySelector('#shortUrl').append(a);
}

// copy 버튼에 이벤트 등록
const copyClick = () => {
  const copyText = document.querySelector('#aTag').innerText;
  console.log(copyText);
  window.prompt('Copy to clipboard: Ctrl+C, Enter', copyText);
};

document.querySelector('#copyButton').addEventListener('click', copyClick);