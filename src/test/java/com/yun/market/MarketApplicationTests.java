//var timeList = ['9:30','10:00','1030','1100','1130','1200','1400']
//        var hgjmgs = [150, 190, 330, 410, 460, 510, 770]
//        var sgjmgs = [160, 221, 310, 400, 488, 535, 666]
//        var bsjmes = [170, 250, 380, 480, 566, 777, 888]
//        $.ajax({
//        url: "/mining/getNorths",
//        success: function (result) {
//        // timeList.push(result.timeList);
//        // hgjmgs.push(result.hgjmgs);
//        // sgjmgs.push(result.sgjmgs);
//        // bsjmes.push(result.bsjmes);
//        },
//        error: function (errorMsg) {
//        alert("获取后台数据失败！");
//        }
//        });
//        var option = {
//        xAxis: {
//        type: 'category',
//        boundaryGap: false,
//        data: timeList
//        },
//        grid: {
//        left: '5%',
//        right: '5%',
//        top: '5%',
//        bottom: '15%'
//        },
//        tooltip: {
//        trigger: 'axis'
//        },
//        yAxis: {
//        type: 'value'
//        },
//        series: [{
//        name: "沪股通",
//        color: '#0d6efd',
//        data: hgjmgs,
//        symbolSize: 0,
//        type: 'line',
//        endLabel: {
//        show: true
//        }
//        },
//        {
//        name: "沪股通",
//        color: '#0dcaf0',
//        data: sgjmgs,
//        symbolSize: 0,
//        type: 'line',
//        endLabel: {
//        show: true
//        }
//        }
//        ,
//        {
//        name: "北上资金",
//        color: '#dc3545',
//        data: bsjmes,
//        symbolSize: 0,
//        type: 'line',
//        endLabel: {
//        show: true
//        }
//        }]
//        };
//
//        option && myChart.setOption(option);
//
//        window.addEventListener("resize", function () {
//        myChart.resize();
//        });
//</script>
//<script src="../assets/dist/js/bootstrap.bundle.min.js"></script>
//<script src="../assets/dist/js/app.js"></script>
//<script src="../assets/dist/js/bootstrap.min.js"></script>
//</body>
//</html>
