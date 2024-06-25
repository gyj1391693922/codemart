<template>
  <div ref="mycahrts" style="height: 600px;"></div>
</template>

<script>
import * as echarts from 'echarts';
import {reactive} from "vue";

export default {
  name: "cm_charts",
  mounted() {
    let option = {
      title:{
      },
      tooltip:{
        formatter:'{b} - {c} 元'
      },
      legend:{
      },
      xAxis:{
        data:['2024-05','2024-06','2024-07','2024-08']
        // data:["累计金额数量","项目","开发者"]
      },
      yAxis:{
      },
      series:[{
        type:'line',
        data:[
          {
            value:21,
            itemStyle:{
              color:'#da8139'
            },
          },{
            value:29,
            itemStyle:{
              color:'#ec5050'
            }
          },{
            value:113,
            itemStyle:{
              color:'#a7e846'
            }
          },0],
          name:["累计金额数量","项目","开发者"],
          color:'#114a9b'
      }
      ]
    }
    echarts.init(this.$refs.mycahrts).setOption(option)
  }
}
</script>

<style scoped>

</style>