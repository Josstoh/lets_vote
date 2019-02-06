/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.josstoh.letsvote.data.repo.deserializers

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import fr.josstoh.letsvote.common.DataOrException
import fr.josstoh.letsvote.config.AppExecutors
import fr.josstoh.letsvote.livedata.DocumentSnapshotOrException
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

internal class DeserializingDocumentSnapshotObserver<T>(
    private val deserializer: DocumentSnapshotDeserializer<T>,
    private val liveData: MediatorLiveData<DataOrException<T, Exception>>
) : Observer<DocumentSnapshotOrException>, KoinComponent {

    private val executors by inject<AppExecutors>()

    override fun onChanged(result: DocumentSnapshotOrException?) {
        if (result != null) {
            val snapshot = result.data
            val exception = result.exception
            if (snapshot != null) {
                // Do this in a thread because DataSnapshot
                // deserialization with reflection can be costly.
                executors.cpuExecutorService.execute {
                    try {
                        val value = deserializer.deserialize(snapshot)
                        liveData.postValue(DataOrException(value, null))
                    }
                    catch (e: Deserializer.DeserializerException) {
                        liveData.postValue(DataOrException(null, e))
                    }
                }
            }
            else if (exception != null) {
                liveData.value = DataOrException(null, exception)
            }
        }
        else {
            liveData.value = null
        }
    }

}
