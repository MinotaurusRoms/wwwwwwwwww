#!/system/bin/sh

busybox mount -o remount,rw /system

echo >> /system/build.prop

sed -i -e "/#Multi Users/d" /system/build.prop
echo "#Multi Users=" >> /system/build.prop

sed -i -e "/fw.max_users/d" /system/build.prop
sed -i -e "/fw.show_multiuserui/d" /system/build.prop
sed -i -e "/fw.show_hidden_users/d" /system/build.prop
sed -i -e "/fw.power_user_switcher/d" /system/build.prop

echo "fw.max_users=10" >> /system/build.prop
echo "fw.show_multiuserui=1" >> /system/build.prop
echo "fw.show_hidden_users=1" >> /system/build.prop
echo "fw.power_user_switcher=1" >> /system/build.prop
