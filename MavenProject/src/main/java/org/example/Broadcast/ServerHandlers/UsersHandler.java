package org.example.Broadcast.ServerHandlers;

import org.example.Broadcast.Broadcasting.Topic;
import org.example.Command.CompositCommands.CompositeCommand;
import org.example.Command.CompositCommands.CompositeCommandImp;
import org.example.Command.FilesCommand.InsertDataCommand;
import org.example.Command.FolderCommand.CreateFolderCommand;
import org.example.Files.FilesInfo;
import org.example.Files.FoldersInfo;
import org.example.Model.User;

public class UsersHandler extends RequestHandler {
    @Override
    public void handleRequest(String topic, Object data) {
        if (topic.equals(Topic.USER.toString())) {
            System.out.println(data.toString());
            receive(data);
        } else if (nextHandler != null) {
            nextHandler.handleRequest(topic, data);
        } else System.out.println("Request cannot be handled");

    }
    @Override
    public void receive(Object user) {
        User tempUser = (User) user;
        CompositeCommand compositeCommand = new CompositeCommandImp();
        compositeCommand.addCommand(new CreateFolderCommand(FoldersInfo.userPath(tempUser.getId())));
        compositeCommand.addCommand(new CreateFolderCommand((FoldersInfo.userPath(tempUser.getId()) + "/DataBases")));
        compositeCommand.addCommand(new InsertDataCommand(FilesInfo.userInfoPath(tempUser.getId()), tempUser));
        compositeCommand.execute();
    }
}
