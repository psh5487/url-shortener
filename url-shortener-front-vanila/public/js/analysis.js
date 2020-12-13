document.addEventListener('DOMContentLoaded', function () {
  getData();
});

function getData () {
  $.ajax({
    type: 'GET',
    url: 'http://localhost:8080/api/url/analysis',
    contentType: 'application/json',
    dataType: 'json',
    success: function (data, response) {
      console.log(data);
      showCount(data.total_count);
      showTop5Chart(data.top5_url_info_list);
      drawPieChart(data.all_url_info_list);
    },
    error: function (request, status, error, data) {
      console.log('code = ' + request.status + ' message = ' + request.responseText + ' error = ' + error);
    }
  });
}

function showCount (totalCount) {
  console.log(totalCount);

  const countsHTML = document.querySelector('#countArea').innerHTML;
  const resultCounts = countsHTML.replace('{count}', totalCount);

  document.querySelector('#count').append(resultCounts);
}

function showTop5Chart (top5InfoList) {
  console.log(top5InfoList);

  const tableHtml = document.querySelector('#tableArea').innerHTML;

  for (let i = 0; i < top5InfoList.length; i++) {
    const resultRow = tableHtml.replace('{idx}', i + 1)
    .replace('{shortUrl}', 'smile.psh/' + top5InfoList[i].short_url)
    .replace('{originalUrl}', top5InfoList[i].long_url)
    .replace('{clickCount}', top5InfoList[i].click_count);

    const products = document.createElement('tr');
    products.innerHTML = resultRow;

    document.querySelector('#tableBody').appendChild(products);
  }
}

function drawPieChart (allInfoList) {
  console.log(allInfoList);

  google.charts.load('current', {packages:['corechart']});
  google.charts.setOnLoadCallback(drawChart);

  // Making Data
  let chartData = [['Short Url', 'Click Count']]

  for (let i in allInfoList) {
    chartData.push(['smile.psh/' + allInfoList[i].short_url, allInfoList[i].click_count]);
  }

  function drawChart () {
    var data = google.visualization.arrayToDataTable(chartData);

    var options = {
      title: 'Click Count',
      pieHole: 0.4,
    };

    var chart = new google.visualization.PieChart(document.getElementById('donutchart'));
    chart.draw(data, options);
  }
}