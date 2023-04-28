package core.domain

import core.model.UserDataModel

interface FirestoreRepository {

    //save user
    fun testfun(userModel: UserDataModel)

    //todo getuser based on uId field documents -> loop -> find the one with field uId matching
}