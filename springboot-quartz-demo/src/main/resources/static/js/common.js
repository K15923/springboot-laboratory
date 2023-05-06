//协议
// let protocol = window.Location.protocol;
// //主机
// let host = window.Location.host
const service = axios.create({
    // baseURL: 'http://localhost:8080/data-backup',
    baseURL: this.window.location.origin + "/data-backup",
    timeout: 5000
});
console.log('---')
service.interceptors.request.use(config => {
    let token = JSON.parse(localStorage.getItem("backup-token"));
    if (token) {
        config.headers.token = token;
    }
    return config;
}, function (error) {
    return Promise.reject(error);
});

service.interceptors.response.use(response => {
    if (response.data.code === 9999) {
        parent.location.href = '/data-backup/';
    }
    if (response.request.responseType == 'arraybuffer') {
        return response;
    }
    return response.data;
});

//获取URL参数
function getQueryString(name) {
    let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    let r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);
    return null;
}

//打开页面
function openPage(title, url, vueObj) {
    parent.layer.open({
        type: 2,
        title: title,
        area: ['800px', '550px'],
        content: url,
        end: function () {
            vueObj.handleQuery();
        }
    });
}

//关闭页面
function closePage() {
    let index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);
}





