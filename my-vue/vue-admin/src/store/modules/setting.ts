import {defineStore} from "pinia";
import {SettingState} from "../interface"
import defaultSettings from "../default/defaultSetting"
const { tagView, fixedHeader, sidebarLogo } = defaultSettings;

const useSettingStore = defineStore("useSettingStore",{
    state:():SettingState =>({
        tagView: tagView,
        fixedHeader: fixedHeader,
        sidebarLogo: sidebarLogo,
    }),
    actions:{
        async changeSetting(data:{key:string, value:any}){
            const {key, value} = data
            switch(key){
                case "tagView":
                    this.tagView = value;
                    break;
                case "fixedHeader":
                    this.fixedHeader = value;
                    break;
                case "sidebarLogo":
                    this.sidebarLogo = value;
                    break;
                default:
                    break;
            }
        },
    },
    getters:{},
    persist:{
        storage:localStorage,
    },
});

export default useSettingStore;

