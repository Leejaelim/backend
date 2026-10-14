package matchuri.backend.groupdecision.repository;

public record GroupRoomMemberCountRow(
        Long roomId,
        long memberCount
) {
}
