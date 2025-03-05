import noProfile from './assets/no-profile.svg'

export default function ProfilePic(){
    return(
        <div className='size-16 flex justify-center items-center '>
            <img className='h-[80%]'
            src={noProfile} 
            />
        </div>
    )
}