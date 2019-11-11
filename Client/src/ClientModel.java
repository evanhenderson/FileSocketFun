public class ClientModel {
    public void sendData(){

    }
    public void receiveData(String serverMsg){
    //server protocol codes: 'login', 'imageList', 'download'
        if (serverMsg == "login"){
            ClientController.setUser();
        }
        if (serverMsg == "imageList"){
            ClientController.imageListSet();
        }
        if (serverMsg == "download"){
            download();
        }
    }
    public void download(){

    }
}
