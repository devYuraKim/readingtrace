import { Fragment, memo } from 'react';

export const FriendItem = memo(({ profile, online, toggleFollow }) => (
  <div className="flex items-center gap-2 border-1 p-2">
    <img src={profile.profileImageUrl} className="rounded-full w-10 h-10" />
    <div>{profile.nickname}</div>
    {online ? '● online' : '○ offline'}
    <div
      className="p-2 cursor-pointer"
      onClick={() => toggleFollow(profile.userId)}
    >
      {profile.isFriend ? 'unfriend' : 'friend'}
    </div>

    <div className="flex gap-2">
      {profile.userReadingStatusDto.map((status) => (
        <Fragment key={status.userReadingStatusId}>
          <img src={status.bookDto.imageLinks} className="w-15 h-full" />
          <div>
            <span>{status.bookDto.title}</span>
            <span>{status.status}</span>
          </div>
        </Fragment>
      ))}
    </div>
  </div>
));
