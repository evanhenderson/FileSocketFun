public class ClientController {

    protected ClientModel model;
    protected ClientView view;

    public ClientController(ClientModel model) {
        this.model = model;
        this.view = new ClientDemoView(this);
    }
}
