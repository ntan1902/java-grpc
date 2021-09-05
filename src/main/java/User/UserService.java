package User;

import com.grpc.user.*;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public class UserService extends UserServiceGrpc.UserServiceImplBase {
    public static final List<User> users = new ArrayList<>();

    @Override
    public void findAllUsers(EmptyRequest request, StreamObserver<AllUsersResponse> responseObserver) {
        AllUsersResponse.Builder response = AllUsersResponse.newBuilder();

        // Find all users
        for (int i = 0; i < users.size(); i++) {
            UserItem userItem = UserItem.newBuilder()
                    .setId(users.get(i).getId())
                    .setName(users.get(i).getName())
                    .setAge(users.get(i).getAge())
                    .build();
            response.addUsers(i, userItem);
        }



        // Response
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void insertUser(UserItem request, StreamObserver<APIResponse> responseObserver) {
        // Insert user
        User user = new User(
                (long) request.getId(),
                request.getName(),
                request.getAge()
        );
        users.add(user);

        // Response
        APIResponse.Builder response = APIResponse.newBuilder();
        response.setResponseCode(201).setResponseMessage("CREATED SUCCESS");

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void findUserById(UserIdRequest request, StreamObserver<UserItem> responseObserver) {
        // Initialize default response
        UserItem.Builder response = UserItem.newBuilder();

        // Find User
        for (User user : users) {
            if (user.getId().equals((long) request.getId())) {
                response
                        .setId(user.getId())
                        .setName(user.getName())
                        .setAge(user.getAge())
                        .build();
            }
        }

        // Response
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void updateUserById(UserItem request, StreamObserver<APIResponse> responseObserver) {
        // Initialize default response
        APIResponse.Builder response = APIResponse.newBuilder();
        response
                .setResponseCode(400)
                .setResponseMessage("Can't find user " + request.getId());

        // Find and update User
        for (User user : users) {
            if (user.getId().equals((long) request.getId())) {
                user.setAge(request.getAge());
                user.setName(request.getName());

                response
                        .setResponseCode(200)
                        .setResponseMessage("UPDATED SUCCESS");
                break;
            }
        }

        // Response
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteUserById(UserIdRequest request, StreamObserver<APIResponse> responseObserver) {
        // Initialize default response
        APIResponse.Builder response = APIResponse.newBuilder();
        response
                .setResponseCode(400)
                .setResponseMessage("Can't find user " + request.getId());

        // Find and remove User
        for (User user : users) {
            if (user.getId().equals((long) request.getId())) {
                users.remove(user);
                response
                        .setResponseCode(200)
                        .setResponseMessage("REMOVED SUCCESS");
                break;
            }
        }

        // Response
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
