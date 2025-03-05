import ProfilePic from "./image"

function Friend(){

    return(
        <div className="flex flex-row w-80 h-16 bg-teal-300">
            <div> 
                <ProfilePic />
            </div>
            <div className="flex flex-row items-center pl-2">
                <label className="mx-2">닉네임</label>
                <label className="mx-2">아이디</label>
            </div>
            <div className="flex flex-row">
                <div>
                    
                </div>
                <div>

                </div>
            </div>
        </div>
        
    )

}

export default function FriendList(){

    return(
        <div>
            <Friend />
        </div>
    )
}